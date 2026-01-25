package com.spring.what.common.database.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.spring.what.**.mapper")
public class MybatisConfig {
}
