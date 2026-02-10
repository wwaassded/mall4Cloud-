package com.spring.what.auth.controller;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.spring.what.api.rbac.feign.PermissionFeignClient;
import com.spring.what.auth.dto.AuthenticationDTO;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthAccountService authAccountService;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private PermissionFeignClient permissionFeignClient;

    @PostMapping("/ua/login")
    @Operation(summary = "账号密码", description = "通过账号登录，还要携带用户的类型，也就是用户所在的系统")
    public ServerResponseEntity<TokenInfoVO> login(
            @Valid @RequestBody AuthenticationDTO authenticationDTO) {
        ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenBOServerResponseEntity = authAccountService.getUserInfoInTokenByInputUserName(authenticationDTO.getPrincipal(),
                authenticationDTO.getCredentials(),
                authenticationDTO.getSysType());
        if (!userInfoInTokenBOServerResponseEntity.isSuccess()) {
            return ServerResponseEntity.transfer(userInfoInTokenBOServerResponseEntity);
        }
        UserInfoInTokenBO userInfoInTokenBO = userInfoInTokenBOServerResponseEntity.getData();
        ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO = new ClearUserPermissionsCacheDTO();
        clearUserPermissionsCacheDTO.setUserId(userInfoInTokenBO.getUserId());
        clearUserPermissionsCacheDTO.setSysType(userInfoInTokenBO.getSysType());
        permissionFeignClient.clearUserPermissionsCache(clearUserPermissionsCacheDTO);
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInTokenBO);
        return ServerResponseEntity.success(tokenInfoVO);
    }

    @PostMapping("/login_out")
    @Operation(summary = "退出登陆", description = "点击退出登陆，清除token，清除菜单缓存")
    public ServerResponseEntity<TokenInfoVO> loginOut() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO = new ClearUserPermissionsCacheDTO();
        clearUserPermissionsCacheDTO.setUserId(userInfoInTokenBO.getUserId());
        clearUserPermissionsCacheDTO.setSysType(userInfoInTokenBO.getSysType());
        permissionFeignClient.clearUserPermissionsCache(clearUserPermissionsCacheDTO);
        tokenStore.deleteAllToken(userInfoInTokenBO.getSysType().toString(), userInfoInTokenBO.getUid());
        return ServerResponseEntity.success();
    }
}
