package com.spring.what.security.config;

import cn.hutool.core.util.ArrayUtil;
import com.spring.what.security.adapter.AuthAdapter;
import com.spring.what.security.adapter.DefaultAuthAdapter;
import com.spring.what.security.filter.AuthFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Collections;

@Configuration
public class AuthConfig {

    @Bean
    @ConditionalOnMissingBean
    public AuthAdapter authAdapter() {
        return new DefaultAuthAdapter();
    }

    @Bean
    @Lazy
    public FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean(AuthAdapter authAdapter, AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(authFilter);
        filterFilterRegistrationBean.addUrlPatterns(ArrayUtil.toArray(authAdapter.pathPatterns(), String.class));
        filterFilterRegistrationBean.setName("auth-filter");
        filterFilterRegistrationBean.setOrder(0);
        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return filterFilterRegistrationBean;
    }
}
