package com.spring.what.leaf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.spring.what.api.**.feign")
@MapperScan("com.spring.what.leaf.mapper")
public class sMallLeafApplication {
    public static void main(String[] args) {
        SpringApplication.run(sMallLeafApplication.class, args);
    }
}
