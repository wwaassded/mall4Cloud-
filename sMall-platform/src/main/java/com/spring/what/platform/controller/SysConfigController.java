package com.spring.what.platform.controller;

import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.model.SysConfig;
import com.spring.what.platform.service.SysConfigService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys_config")
public class SysConfigController {


    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取保存支付宝支付配置信息
     */
    @GetMapping("/info/{key}")
    public ServerResponseEntity<String> info(@PathVariable String key) {
        return ServerResponseEntity.success(sysConfigService.getInfoByKey(key));
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig sysConfig) {
        sysConfigService.save(sysConfig);
        return ServerResponseEntity.success();
    }


}
