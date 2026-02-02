package com.spring.what.auth.controller;

import com.spring.what.auth.manager.TokenStore;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "token")
public class TokenController {

    @Resource
    private TokenStore tokenStore;

//    @PostMapping("/ua/token/refresh")
//    public ServerResponseEntity<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
//    }
}
