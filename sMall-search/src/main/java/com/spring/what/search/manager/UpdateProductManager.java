package com.spring.what.search.manager;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.alibaba.fastjson.JSON;
import com.spring.what.api.product.bo.EsProductBO;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.search.constant.EsIndexEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UpdateProductManager {

    @Resource
    private ElasticsearchClient myElasticSearchClient;

    public void esUpdateSpuBySpuIds(List<Long> spuIds, EsProductBO esProductBO) {
        String source = JSON.toJSONString(esProductBO);
        try {
            BulkRequest bulkRequest = getBulkRequest(spuIds, source);
            BulkResponse bulkResponse = myElasticSearchClient.bulk(bulkRequest);
            if (bulkResponse.errors()) {
                throw new Mall4cloudException(bulkResponse.toString());
            }
        } catch (IOException e) {
            throw new Mall4cloudException("", e);
        }
    }

    private static BulkRequest getBulkRequest(List<Long> spuIds, String source) {
        BulkRequest.Builder builder = new BulkRequest.Builder();
        for (Long spuId : spuIds) {
            builder.operations(op -> op
                    .update(u -> u
                            .index(EsIndexEnum.PRODUCT.value())
                            .id(String.valueOf(spuId))
                            .action(a -> a
                                    .doc(source))
                    ));
        }
        return builder.build();
    }

}
