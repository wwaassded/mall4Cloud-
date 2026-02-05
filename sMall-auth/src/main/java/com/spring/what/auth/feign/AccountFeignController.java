package com.spring.what.auth.feign;

import cn.hutool.core.util.StrUtil;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.api.leaf.feign.LeafFeignClient;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.auth.model.AuthAccount;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.common.util.PrincipalUtil;
import com.spring.what.security.AuthContext;
import com.spring.what.security.bo.AuthAccountInVerifyBO;
import com.spring.what.security.constant.InputUsernameEnum;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccountFeignController implements AccountFeignClient {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private AuthAccountService authAccountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private LeafFeignClient leafFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Long> save(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<Long> leafResponse = leafFeignClient.getSegmentId("mall4cloud-auth-account");
        if (!leafResponse.isSuccess()) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        ServerResponseEntity<AuthAccount> verifiedResponse = verify(authAccountDTO);
        if (!verifiedResponse.isSuccess()) {
            return ServerResponseEntity.transfer(verifiedResponse);
        }
        AuthAccount authAccount = verifiedResponse.getData();
        authAccount.setUid(leafResponse.getData());
        authAccountService.save(authAccount);
        return ServerResponseEntity.success(authAccount.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> update(AuthAccountDTO authAccountDTO) {
        ServerResponseEntity<AuthAccount> verified = verify(authAccountDTO);
        if (!verified.isSuccess()) {
            return ServerResponseEntity.transfer(verified);
        }
        authAccountService.updateAuthAccount(verified.getData());
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateAuthAccountStatus(AuthAccountDTO authAccountDTO) {
        if (Objects.isNull(authAccountDTO.getStatus())) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        AuthAccount authAccount = BeanUtil.map(authAccountDTO, AuthAccount.class);
        authAccountService.updateAuthAccount(authAccount);
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deleteByUserIdAndSysType(Long userId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        authAccountService.deleteByUserIdAndSysType(userId, userInfoInTokenBO.getSysType());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId, Integer sysType) {
        AuthAccount authAccount = authAccountService.getAuthAcccountByUserIdAndSysType(userId, sysType);
        return ServerResponseEntity.success(BeanUtil.map(authAccount, AuthAccountVO.class));
    }

    @Override
    public ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUsernameAndSysType(String username, SysTypeEnum sysType) {
        return ServerResponseEntity.success(authAccountService.getAuthAcccountByUserNameAndSysType(username, sysType));
    }

    @Override
    public ServerResponseEntity<Void> updateUserInfoByUserIdAndSysType(UserInfoInTokenBO userInfoInTokenBO, Long userId, Integer sysType) {
        AuthAccount byUserIdAndSysType = authAccountService.getAuthAcccountByUserIdAndSysType(userId, sysType);
        userInfoInTokenBO.setUid(byUserIdAndSysType.getUid());
        tokenStore.updateUserInfoByUidAndAppId(byUserIdAndSysType.getUid(), sysType.toString(), userInfoInTokenBO);
        AuthAccount authAccount = BeanUtil.map(userInfoInTokenBO, AuthAccount.class);
        int res = authAccountService.updateByIdAndSystype(authAccount, userId, sysType);
        if (res == -1) {
            throw new Mall4cloudException("用户信息错误，更新失败");
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getMerchantInfoByTenantId(Long tenantId) {
        return ServerResponseEntity.success(authAccountService.getMerchantInfoByTenantId(tenantId));
    }

    @Override
    public ServerResponseEntity<Void> updateShopPassword(AuthAccountDTO authAccountDTO) {
        if (StrUtil.isNotBlank(authAccountDTO.getPassword())) {
            authAccountDTO.setPassword(passwordEncoder.encode(authAccountDTO.getPassword()));
        }
        authAccountService.updateShopPassword(authAccountDTO.getUserId(), authAccountDTO.getSysType(), authAccountDTO.getPassword());
        return ServerResponseEntity.success();
    }

    private ServerResponseEntity<AuthAccount> verify(AuthAccountDTO authAccountDTO) {
        if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
            return ServerResponseEntity.showFail("输入的用户名不符合格式");
        }
        AuthAccountInVerifyBO authAccountInVerifyBO = authAccountService.getVerifiedAuthAccount(InputUsernameEnum.USERNAME.getValue(), authAccountDTO.getUsername(), authAccountDTO.getSysType());
        if (authAccountInVerifyBO != null && !Objects.equals(authAccountInVerifyBO.getUserId(), authAccountDTO.getUserId())) {
            return ServerResponseEntity.showFail("输入的用户名已经存在了");
        }
        AuthAccount authAccount = BeanUtil.map(authAccountDTO, AuthAccount.class);
        if (authAccount.getPassword() != null) {
            authAccount.setPassword(passwordEncoder.encode(authAccount.getPassword()));
        }
        return ServerResponseEntity.success(authAccount);
    }
}
