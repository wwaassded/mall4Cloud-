package com.spring.what.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.auth.constant.AuthAccountStatusEnum;
import com.spring.what.auth.model.AuthAccount;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.auth.mapper.AuthAccountMapper;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.common.util.PrincipalUtil;
import com.spring.what.security.bo.AuthAccountInVerifyBO;
import com.spring.what.security.constant.InputUsernameEnum;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【auth_account(统一账户信息)】的数据库操作Service实现
 * @createDate 2026-01-31 11:19:54
 */
@Service
public class AuthAccountServiceImpl extends ServiceImpl<AuthAccountMapper, AuthAccount>
        implements AuthAccountService {

    @Resource
    private AuthAccountMapper authAccountMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserName(String principal, String credentials, Integer sysType) {
        if (StrUtil.isBlank(principal)) {
            return ServerResponseEntity.showFail("输入用户名不能为空");
        }
        if (StrUtil.isBlank(credentials)) {
            return ServerResponseEntity.showFail("输入密码不能为空");
        }
        InputUsernameEnum inputUsernameEnum = null;
        if (PrincipalUtil.isUserName(principal)) {
            inputUsernameEnum = InputUsernameEnum.USERNAME;
        }
        if (inputUsernameEnum == null) {
            return ServerResponseEntity.showFail("请输入正确格式的用户名");
        }
        AuthAccountInVerifyBO authAccountInVerifyBO = authAccountMapper.getVerifiedUserInfoByInputUserName(principal, InputUsernameEnum.USERNAME.value, sysType);
        if (authAccountInVerifyBO == null) {
            //TODO 如果没有查找到正确的user 应该有额外的处理
            return ServerResponseEntity.showFail("用户名或密码输入错误");
        }
        if (!passwordEncoder.matches(authAccountInVerifyBO.getPassword(), credentials)) {
            return ServerResponseEntity.showFail("用户名或密码输入错误");
        }
        if (Objects.equals(authAccountInVerifyBO.getStatus(), AuthAccountStatusEnum.DISABLE.value())) {
            return ServerResponseEntity.showFail("用户已经被封禁请联系客服");
        }
        return ServerResponseEntity.success(BeanUtil.map(authAccountInVerifyBO, UserInfoInTokenBO.class));
    }

    @Override
    public AuthAccount getUserInfoInTokenByIdAndSysType(Long userId, Integer sysType) {
        LambdaQueryWrapper<AuthAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AuthAccount::getUserId, userId)
                .eq(AuthAccount::getSysType, sysType);
        return authAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public void updatePassword(Long userId, Integer sysType, String encode) {
        LambdaUpdateWrapper<AuthAccount> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AuthAccount::getUserId, userId)
                .eq(AuthAccount::getSysType, sysType)
                .set(AuthAccount::getPassword, encode);
    }

    @Override
    public AuthAccountInVerifyBO getVerifiedAuthAccount(Integer value, String username, Integer sysType) {
        return authAccountMapper.getVerifiedUserInfoByInputUserName(username, value, sysType);
    }

    @Override
    public void updateAuthAccount(AuthAccount data) {
        authAccountMapper.updateAuthAccount(data);
    }

    @Override
    public void deleteByUserIdAndSysType(Long userId, Integer sysType) {
        authAccountMapper.deleteByUserIdAndSysType(userId, sysType);
    }

    @Override
    public AuthAccount getAuthAcccountByUserIdAndSysType(Long userId, Integer sysType) {
        return authAccountMapper.getAuthAcccountByUserIdAndSysType(userId, sysType);
    }

    @Override
    public AuthAccountVO getAuthAcccountByUserNameAndSysType(String username, SysTypeEnum sysType) {
        return authAccountMapper.getAuthAcccountByUserNameAndSysType(username, sysType.value());
    }

    @Override
    public int updateByIdAndSystype(AuthAccount authAccount, Long userId, Integer sysType) {
        return authAccountMapper.updateByIdAndSystype(authAccount, userId, sysType);
    }

    @Override
    public AuthAccountVO getMerchantInfoByTenantId(Long tenantId) {
        LambdaQueryWrapper<AuthAccount> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AuthAccount::getTenantId, tenantId)
                .eq(AuthAccount::getIsAdmin, 1)
                .eq(AuthAccount::getSysType, SysTypeEnum.MULTISHOP.value());
        AuthAccount authAccount = authAccountMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.map(authAccount, AuthAccountVO.class);
    }

    @Override
    public void updateShopPassword(Long userId, Integer sysType, String password) {
        LambdaUpdateWrapper<AuthAccount> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AuthAccount::getUserId, userId)
                .eq(AuthAccount::getSysType, sysType)
                .set(AuthAccount::getPassword, password);
        authAccountMapper.update(lambdaUpdateWrapper);
    }
}




