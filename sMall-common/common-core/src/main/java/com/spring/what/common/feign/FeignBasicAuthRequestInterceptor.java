package com.spring.what.common.feign;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@ConditionalOnBean({RequestInterceptor.class})
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    @Resource
    private FeignInsideAuthConfig insideAuthConfig;

    @Override
    public void apply(RequestTemplate template) {
        template.header(insideAuthConfig.getKey(), insideAuthConfig.getSecret());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(authorization)) {
            template.header("Authorization", authorization);
        }
    }

}
