package com.spring.what.api.auth.feign;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.common.constant.Auth;
import com.spring.what.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall4cloud-auth", contextId = "token")
public interface TokenFeignClient {

    @GetMapping(value = Auth.CHECK_TOKEN_URI)
    ServerResponseEntity<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);
}
