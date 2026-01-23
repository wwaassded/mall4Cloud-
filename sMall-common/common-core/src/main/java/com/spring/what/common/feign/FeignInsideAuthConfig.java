package com.spring.what.common.feign;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties("feign.inside")
public class FeignInsideAuthConfig {

    public static final String FEIGN_INSIDE_URL_PREFIX = "/feign";

    @Value("${feign.inside.key}")
    private String key;

    @Value("${feign.inside.secret}")
    private String secret;

    @Value("#{'${feign.inside.ips:}'.split(',')}")
    private List<String> ips;

    @Override
    public String toString() {
        return "FeignInsideAuthConfig{" +
                "key='" + key + '\'' +
                ", secret='" + secret + '\'' +
                ", ips=" + ips +
                '}';
    }
}
