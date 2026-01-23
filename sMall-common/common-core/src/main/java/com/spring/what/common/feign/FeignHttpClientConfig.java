package com.spring.what.common.feign;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHttpClientConfig {

    @Bean(destroyMethod = "close")
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(400);
        manager.setDefaultMaxPerRoute(100);
        RequestConfig requestConfig = RequestConfig.custom()
                //从连接池获取连接等待超时时间
                .setConnectionRequestTimeout(2000)
                //请求超时时间
                .setConnectTimeout(2000)
                //等待服务响应超时时间
                .setSocketTimeout(15000)
                .build();
        return HttpClientBuilder.create()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .build();
    }
}
