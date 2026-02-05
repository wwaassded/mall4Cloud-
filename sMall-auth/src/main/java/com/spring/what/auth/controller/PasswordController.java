package com.spring.what.auth.controller;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.auth.dto.UpdatePasswordDTO;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.auth.model.AuthAccount;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

    @Resource
    private AuthAccountService authAccountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenStore tokenStore;

    @PutMapping("/update_password")
    @Operation(summary = "更新密码", description = "更新当前用户的密码, 更新密码之后要退出登录，清理token")
    public ServerResponseEntity<TokenInfoVO> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        AuthAccount authAccount = authAccountService.getUserInfoInTokenByIdAndSysType(userInfoInTokenBO.getUserId(), userInfoInTokenBO.getSysType());
        if (!passwordEncoder.matches(authAccount.getPassword(), updatePasswordDTO.getOldPassword())) {
            return ServerResponseEntity.showFail("输入的旧密码有误");
        }
        authAccountService.updatePassword(userInfoInTokenBO.getUserId(), userInfoInTokenBO.getSysType(), passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        tokenStore.deleteAllToken(userInfoInTokenBO.getSysType().toString(), userInfoInTokenBO.getUid());
        return ServerResponseEntity.success();
    }
}
