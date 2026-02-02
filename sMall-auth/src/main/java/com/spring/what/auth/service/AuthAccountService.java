package com.spring.what.auth.service;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.auth.model.AuthAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.common.response.ServerResponseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author whatyi
 * @description 针对表【auth_account(统一账户信息)】的数据库操作Service
 * @createDate 2026-01-31 11:19:54
 */
public interface AuthAccountService extends IService<AuthAccount> {

    ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserName(@NotBlank(message = "principal不能为空") String principal, @NotBlank(message = "credentials不能为空") String credentials, @NotNull(message = "sysType不能为空") Integer sysType);
}
