package com.spring.what.auth.manager;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.PrincipalUtil;
import com.spring.what.security.bo.TokenInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RefreshScope
public class TokenStore {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RedisSerializer<Object> redisSerializer;

    private final StringRedisTemplate stringRedisTemplate;

    public TokenStore(RedisTemplate<Object, Object> objectTemplate,
                      RedisSerializer<Object> redisSerializer,
                      StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = objectTemplate;
        this.redisSerializer = redisSerializer;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public TokenInfoBO storeAccessToken(UserInfoInTokenBO userInfoInTokenBO) {
        TokenInfoBO tokenInfoBO = new TokenInfoBO();
        tokenInfoBO.setUserInfoInToken(userInfoInTokenBO);
        tokenInfoBO.setExpiresIn(getExpiresIn(userInfoInTokenBO.getSysType()));
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();
        //通过uid查找accessToken 方便实现用户的登出 拉黑 下号等功能的实现
        String uidToAccessTokenKey = getUidToAccessKey(getApprovalKey(userInfoInTokenBO));
        //通过accessToken查找用户的基本信息
        String accessKeyStr = getAccessKey(accessToken);
        //通过refresh Token 可以查找到所有名下的access token 方便对用户通过refreshToken来刷新自己的accessToken
        String refreshToTokenKey = getRefreshToAccessKey(refreshToken);
        List<String> readyToAddList = new ArrayList<>();
        readyToAddList.add(accessToken + StrUtil.COLON + refreshToken);
        //登录用户保持活跃 对用户名下的accessToken进行时间上的更新
        Long size = redisTemplate.opsForSet().size(uidToAccessTokenKey);
        if (size != null && size > 0L) {
            List<String> popedValueList = stringRedisTemplate.opsForSet().pop(uidToAccessTokenKey, size);
            if (popedValueList != null && !popedValueList.isEmpty()) {
                for (String accessTokenWithRefreshToken : popedValueList) {
                    String[] strings = accessTokenWithRefreshToken.split(StrUtil.COLON);
                    String access = strings[0];
                    if (BooleanUtil.isTrue(redisTemplate.hasKey(getAccessKey(access)))) {
                        readyToAddList.add(accessTokenWithRefreshToken);
                    }
                }
            }
        }
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            Integer expiresIn = tokenInfoBO.getExpiresIn();
            byte[] uid = uidToAccessTokenKey.getBytes(StandardCharsets.UTF_8);
            byte[] accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] refreshToTokenKeyBytes = refreshToTokenKey.getBytes(StandardCharsets.UTF_8);

            for (String value : readyToAddList) {
                connection.setCommands().sAdd(uid, value.getBytes(StandardCharsets.UTF_8));
            }

            connection.keyCommands().expire(uid, expiresIn);

            connection.stringCommands().setEx(refreshToTokenKeyBytes, expiresIn, accessKey);

            connection.stringCommands().setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(userInfoInTokenBO)));

            return null;
        });
        tokenInfoBO.setAccessToken(encryptToken(accessToken, userInfoInTokenBO.getSysType()));
        tokenInfoBO.setRefreshToken(encryptToken(refreshToken, userInfoInTokenBO.getSysType()));
        return tokenInfoBO;
    }

    public ServerResponseEntity<UserInfoInTokenBO> getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
        if (accessToken == null) {
            return ServerResponseEntity.showFail("accessToken 不能是null值");
        }
        String realAccessToken;
        if (needDecrypt) {
            ServerResponseEntity<String> serverResponseEntity = decryptToken(accessToken);
            if (!serverResponseEntity.isSuccess()) {
                return ServerResponseEntity.transfer(serverResponseEntity);
            }
            realAccessToken = serverResponseEntity.getData();
        } else {
            realAccessToken = accessToken;
        }
        UserInfoInTokenBO userInfoInToken = (UserInfoInTokenBO) redisTemplate.opsForValue().get(getAccessKey(realAccessToken));
        if (userInfoInToken == null) {
            return ServerResponseEntity.showFail("accessToken已经过期了");
        } else {
            return ServerResponseEntity.success(userInfoInToken);
        }
    }

    private ServerResponseEntity<String> decryptToken(String data) {
        String decryptStr;
        String decryptToken;
        try {
            decryptStr = Base64.decodeStr(data);
            decryptToken = decryptStr.substring(0, 32);
            // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
            long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));
            // 系统类型
            int sysType = Integer.parseInt(decryptStr.substring(45));
            // token的过期时间
            int expiresIn = getExpiresIn(sysType);
            long second = 1000L;
            if (System.currentTimeMillis() - createTokenTime > expiresIn * second) {
                return ServerResponseEntity.showFail("token 格式有误");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ServerResponseEntity.showFail("token 格式有误");
        }

        // 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
        if (!PrincipalUtil.isSimpleChar(decryptToken)) {
            return ServerResponseEntity.showFail("token 格式有误");
        }
        return ServerResponseEntity.success(decryptToken);
    }


    public ServerResponseEntity<TokenInfoBO> refreshToken(String refreshToken) {
        if (StrUtil.isBlank(refreshToken)) {
            return ServerResponseEntity.showFail("refreshToken不能是null值");
        }
        ServerResponseEntity<String> serverResponseEntity = decryptToken(refreshToken);
        if (!serverResponseEntity.isSuccess()) {
            return ServerResponseEntity.transfer(serverResponseEntity);
        }
        String realRefreshToken = serverResponseEntity.getData();
        String accessToken = stringRedisTemplate.opsForValue().get(getRefreshToAccessKey(realRefreshToken));
        if (StrUtil.isBlank(accessToken)) {
            return ServerResponseEntity.showFail("refreshToken已经过期了");
        }
        ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenBOResponse = getUserInfoByAccessToken(accessToken, false);
        if (!userInfoInTokenBOResponse.isSuccess()) {
            return ServerResponseEntity.showFail("refreshToken已经过期了");
        }
        UserInfoInTokenBO userInfoInTokenBO = userInfoInTokenBOResponse.getData();
        redisTemplate.delete(getRefreshToAccessKey(realRefreshToken));
        redisTemplate.delete(getAccessKey(accessToken));
        TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInTokenBO);
        return ServerResponseEntity.success(tokenInfoBO);
    }

    public void deleteAllToken(String appId, Long uid) {
        String uidToAccessKey = getUidToAccessKey(getApprovalKey(appId, uid));
        Long size = redisTemplate.opsForSet().size(uidToAccessKey);
        if (size == null || size == 0L) {
            return;
        }
        List<String> readyToDel = stringRedisTemplate.opsForSet().pop(uidToAccessKey, size);
        if (readyToDel != null && !readyToDel.isEmpty()) {
            for (String accessWithRefreshToken : readyToDel) {
                String[] accessWithRefreshTokenArray = accessWithRefreshToken.split(StrUtil.COLON);
                String accessToken = accessWithRefreshTokenArray[0];
                String refreshToken = accessWithRefreshTokenArray[1];
                redisTemplate.delete(getRefreshToAccessKey(refreshToken));
                redisTemplate.delete(getAccessKey(accessToken));
            }
        }
        redisTemplate.delete(uidToAccessKey);
    }

    public TokenInfoVO storeAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInTokenBO);
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
        return tokenInfoVO;
    }

    public void updateUserInfoByUidAndAppId(Long uid, String appId, UserInfoInTokenBO userInfoInTokenBO) {
        if (userInfoInTokenBO == null) {
            return;
        }
        String uidToAccessKey = getUidToAccessKey(getApprovalKey(appId, uid));
        Set<String> searchList = stringRedisTemplate.opsForSet().members(uidToAccessKey);
        if (searchList == null || searchList.isEmpty()) {
            throw new Mall4cloudException(ResponseEnum.UNAUTHORIZED);
        }
        for (String accessWithRefreshToken : searchList) {
            String[] accessWithRefreshTokenArray = accessWithRefreshToken.split(StrUtil.COLON);
            String accessKey = getAccessKey(accessWithRefreshTokenArray[0]);
            UserInfoInTokenBO oldUserInfoTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue().get(accessKey);
            if (oldUserInfoTokenBO == null) {
                continue;
            }
            redisTemplate.opsForValue().set(accessKey,
                    Objects.requireNonNull(redisSerializer.serialize(userInfoInTokenBO)),
                    getExpiresIn(userInfoInTokenBO.getSysType()),
                    TimeUnit.SECONDS);
        }
    }

    private int getExpiresIn(int sysType) {
        // 3600秒
        int expiresIn = 3600;

        // 普通用户token过期时间 1小时
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        // 系统管理员的token过期时间 2小时
        if (Objects.equals(sysType, SysTypeEnum.MULTISHOP.value()) || Objects.equals(sysType, SysTypeEnum.PLATFORM.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }

    private static String getApprovalKey(UserInfoInTokenBO userInfoInToken) {
        return getApprovalKey(userInfoInToken.getSysType().toString(), userInfoInToken.getUid());
    }

    private static String getApprovalKey(String appId, Long uid) {
        return uid == null ? appId : appId + StrUtil.COLON + uid;
    }

    public String getAccessKey(String accessToken) {
        return CacheNames.ACCESS + accessToken;
    }

    public String getUidToAccessKey(String approvalKey) {
        return CacheNames.UID_TO_ACCESS + approvalKey;
    }

    public String getRefreshToAccessKey(String refreshToken) {
        return CacheNames.REFRESH_TO_ACCESS + refreshToken;
    }


    private String encryptToken(String accessToken, Integer sysType) {
        return Base64.encode(accessToken + System.currentTimeMillis() + sysType);
    }


}
