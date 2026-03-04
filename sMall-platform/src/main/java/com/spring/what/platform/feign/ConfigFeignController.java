package com.spring.what.platform.feign;

import com.spring.what.api.platform.feign.ConfigFeignClient;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.service.SysConfigService;
import jakarta.annotation.Resource;

public class ConfigFeignController implements ConfigFeignClient {

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public ServerResponseEntity<String> getConfig(String key) {
        return ServerResponseEntity.success(sysConfigService.getInfoByKey(key));
    }
}
