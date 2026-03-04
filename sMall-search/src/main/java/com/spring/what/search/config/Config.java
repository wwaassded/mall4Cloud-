package com.spring.what.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean("myElasticSearchClient")
    ElasticsearchClient elasticsearchClient() {
        return ElasticsearchClient.of(b ->
                b.apiKey("")
                        .host("localhost:3333"));
    }
}
