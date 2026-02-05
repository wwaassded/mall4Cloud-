package com.spring.what.auth.feign;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.common.response.ServerResponseEntity;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountFeignController implements AccountFeignClient {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private AuthAccountService authAccountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public ServerResponseEntity<Long> save(AuthAccountDTO authAccountDTO) {
        return null;
    }

    @Override
    public ServerResponseEntity<Void> update(AuthAccountDTO authAccountDTO) {
        return null;
    }

    @Override
    public ServerResponseEntity<Void> updateAuthAccountStatus(AuthAccountDTO authAccountDTO) {
        return null;
    }

    @Override
    public ServerResponseEntity<Void> deleteByUserIdAndSysType(Long userId) {
        return null;
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUserIdAndSysType(Long userId, Integer sysType) {
        return null;
    }

    @Override
    public ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        return null;
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUsernameAndSysType(String username, SysTypeEnum sysType) {
        return null;
    }

    @Override
    public ServerResponseEntity<Void> updateUserInfoByUserIdAndSysType(UserInfoInTokenBO userInfoInTokenBO, Long userId, Integer sysType) {
        return null;
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getMerchantInfoByTenantId(Long tenantId) {
        return null;
    }

    @Override
    public ServerResponseEntity<Void> updateShopPassword(AuthAccountDTO authAccountDTO) {
        return null;
    }
}
