package com.spring.what.auth.service;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.auth.model.AuthAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.bo.AuthAccountInVerifyBO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author whatyi
 * @description 针对表【auth_account(统一账户信息)】的数据库操作Service
 * @createDate 2026-01-31 11:19:54
 */
public interface AuthAccountService extends IService<AuthAccount> {

    ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserName(@NotBlank(message = "principal不能为空") String principal, @NotBlank(message = "credentials不能为空") String credentials, @NotNull(message = "sysType不能为空") Integer sysType);

    AuthAccount getUserInfoInTokenByIdAndSysType(Long userId, Integer sysType);

    void updatePassword(Long userId, Integer sysType, String encode);

    AuthAccountInVerifyBO getVerifiedAuthAccount(Integer value, @NotBlank(message = "username not blank") String username, @NotNull(message = "sysType not null") Integer sysType);

    void updateAuthAccount(AuthAccount data);

    void deleteByUserIdAndSysType(Long userId, Integer sysType);

    AuthAccount getAuthAcccountByUserIdAndSysType(Long userId, Integer sysType);

    AuthAccountVO getAuthAcccountByUserNameAndSysType(String username, SysTypeEnum sysType);

    int updateByIdAndSystype(AuthAccount authAccount, Long userId, Integer sysType);

    AuthAccountVO getMerchantInfoByTenantId(Long tenantId);

    void updateShopPassword(@NotNull(message = "userId not null") Long userId, @NotNull(message = "sysType not null") Integer sysType, String password);
}
