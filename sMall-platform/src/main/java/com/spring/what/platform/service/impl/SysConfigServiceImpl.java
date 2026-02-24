package com.spring.what.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.platform.model.SysConfig;
import com.spring.what.platform.service.SysConfigService;
import com.spring.what.platform.mapper.SysConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author whatyi
 * @description 针对表【sys_config(系统配置信息表)】的数据库操作Service实现
 * @createDate 2026-02-21 18:56:26
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
        implements SysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#key")
    public String getInfoByKey(String key) {
        LambdaQueryWrapper<SysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysConfig::getParamKey, key);
        SysConfig sysConfig = sysConfigMapper.selectOne(lambdaQueryWrapper);
        return sysConfig == null ? null : sysConfig.getParamValue();
    }
}




