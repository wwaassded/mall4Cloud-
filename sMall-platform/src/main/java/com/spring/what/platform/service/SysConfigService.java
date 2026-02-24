package com.spring.what.platform.service;

import com.spring.what.platform.model.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author whatyi
* @description 针对表【sys_config(系统配置信息表)】的数据库操作Service
* @createDate 2026-02-21 18:56:26
*/
public interface SysConfigService extends IService<SysConfig> {

    String getInfoByKey(String key);
}
