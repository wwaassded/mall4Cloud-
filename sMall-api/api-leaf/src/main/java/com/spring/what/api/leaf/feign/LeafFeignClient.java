package com.spring.what.api.leaf.feign;

import com.spring.what.common.feign.FeignInsideAuthConfig;
import com.spring.what.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sMall-leaf", contextId = "segment")
public interface LeafFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/segment")
    ServerResponseEntity<Long> getSegmentId(@RequestParam("key") String key);

}
