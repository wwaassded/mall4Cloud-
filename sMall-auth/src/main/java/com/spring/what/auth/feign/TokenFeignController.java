package com.spring.what.auth.feign;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.feign.TokenFeignClient;
import com.spring.what.auth.manager.TokenStore;
import com.spring.what.common.response.ServerResponseEntity;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenFeignController implements TokenFeignClient {

    @Resource
    private TokenStore tokenStore;

    @Override
    public ServerResponseEntity<UserInfoInTokenBO> checkToken(String accessToken) {
        return tokenStore.getUserInfoByAccessToken(accessToken, true);
    }
}
