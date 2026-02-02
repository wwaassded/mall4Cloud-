package com.spring.what.auth.controller;

import com.spring.what.api.auth.vo.TokenInfoVO;
import com.spring.what.auth.dto.RefreshTokenDTO;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.bo.TokenInfoBO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import com.spring.what.common.util.BeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "token")
public class TokenController {

    @Resource
    private TokenStore tokenStore;

    @PostMapping("/ua/token/refresh")
    public ServerResponseEntity<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        ServerResponseEntity<TokenInfoBO> serverResponseEntity = tokenStore.refreshToken(refreshTokenDTO.getRefreshToken());
        if (!serverResponseEntity.isSuccess()) {
            return ServerResponseEntity.transfer(serverResponseEntity);
        } else {
            TokenInfoBO tokenInfoBO = serverResponseEntity.getData();
            return ServerResponseEntity.success(BeanUtil.map(tokenInfoBO, TokenInfoVO.class));
        }
    }
}
