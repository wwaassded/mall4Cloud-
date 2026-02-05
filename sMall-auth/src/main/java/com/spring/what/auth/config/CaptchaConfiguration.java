package com.spring.what.auth.config;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.common.Const;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
class CaptchaConfiguration {
    @Bean
    CaptchaService captchaService() {
        Properties properties = new Properties();
        properties.put(Const.CAPTCHA_CACHETYPE, "redis");
        properties.put(Const.CAPTCHA_WATER_MARK, "");
        properties.put(Const.CAPTCHA_TYPE, CaptchaTypeEnum.BLOCKPUZZLE.getCodeValue());
        return CaptchaServiceFactory.getInstance(properties);
    }
}
