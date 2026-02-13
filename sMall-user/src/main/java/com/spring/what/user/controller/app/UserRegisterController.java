package com.spring.what.user.controller.app;

import cn.hutool.core.util.StrUtil;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.user.dto.UserRegisterDTO;
import com.spring.what.user.model.User;
import com.spring.what.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ua/user/register")
@Tag(name = "app-用户注册接口")
public class UserRegisterController {

    @Resource
    private UserService userService;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Operation(summary = "注册")
    @PostMapping
    public ServerResponseEntity<TokenInfoVO> register(@Valid @RequestBody UserRegisterDTO param) {
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        if (StrUtil.isBlank(param.getNickName())) {
            param.setNickName(param.getUserName());
        }
        Long uid = userService.saveFromUSerREgisterDTO(param);
        userInfoInTokenBO.setUid(uid);
        userInfoInTokenBO.setUserId(param.getUserId());
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);
        return accountFeignClient.storeTokenAndGetVo(userInfoInTokenBO);
    }

}