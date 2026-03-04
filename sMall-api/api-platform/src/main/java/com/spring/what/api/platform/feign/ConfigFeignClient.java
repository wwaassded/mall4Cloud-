package com.spring.what.api.platform.feign;

import com.spring.what.common.feign.FeignInsideAuthConfig;
import com.spring.what.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sMall-platform", contextId = "config")
public interface ConfigFeignClient {


    /**
     * 获取配置信息
     *
     * @param key key
     * @return 配置信息json
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/config/getConfig")
    ServerResponseEntity<String> getConfig(@RequestParam("key") String key);

}
