package com.spring.what.search.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.spring.what.common.response.ServerResponseEntity;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ua/test/")
public class TestController {

    @Resource
    private ElasticsearchClient myElasticSearchClient;


    @GetMapping
    public ServerResponseEntity<Void> testGet() throws IOException {
        SearchResponse<Void> voidSearchResponse = myElasticSearchClient.search(s -> s
                .index("user_index")
                .size(0)
                .query(q -> q
                        .range(r -> r
                                .number(n -> n
                                        .field("age")
                                        .lte(18.0)
                                )
                        )
                )
                .aggregations("nation", a -> a
                        .terms(t -> t
                                .field("nation")
                        )
                        .aggregations("avg", avg -> avg
                                .avg(av -> av
                                        .field("age")
                                )
                        )
                ), Void.class
        );
        Aggregate nation = voidSearchResponse.aggregations().get("nation");
        StringTermsAggregate sterms = nation.sterms();
        Buckets<StringTermsBucket> buckets = sterms.buckets();
        for (StringTermsBucket stringTermsBucket : buckets.array()) {
            System.out.println(stringTermsBucket.key()); // nation
            System.out.println(stringTermsBucket.docCount()); // 相同nation的doc总数
            Aggregate avg = stringTermsBucket.aggregations().get("avg");
            AvgAggregate avgAggregate = avg.avg();
            System.out.println(avgAggregate.value()); //年龄的平均值
        }
        return ServerResponseEntity.success();
    }
}
