package com.spring.what.search.manager;

import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.alibaba.fastjson2.JSON;
import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.EsOrderVO;
import com.spring.what.common.dto.OrderSearchDTO;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.util.BooleanUtil;
import com.spring.what.search.constant.EsIndexEnum;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OrderSearchManager {

    private static final Logger log = LoggerFactory.getLogger(OrderSearchManager.class);

    @Resource
    private ElasticsearchClient myElasticSearchClient;


    public EsPageVO<EsOrderVO> pageSearchResult(EsPageDTO pageDTO, OrderSearchDTO orderSearchDTO) {
        EsPageVO<EsOrderVO> voEsPageVO;
        SearchRequest searchRequest = buildSearchRequest(pageDTO, orderSearchDTO);
        try {
            SearchResponse<EsOrderVO> searchResponse = myElasticSearchClient.search(searchRequest, EsOrderVO.class);
            log.info("search的结果是：{}", searchResponse.toString());
            voEsPageVO = buildResponse(pageDTO, searchResponse);
        } catch (IOException e) {
            log.error(e.toString());
            throw new Mall4cloudException("服务器出了点小差，请稍后重试", e);
        }
        return voEsPageVO;
    }

    private EsPageVO<EsOrderVO> buildResponse(EsPageDTO pageDTO, SearchResponse<EsOrderVO> searchResponse) {
        EsPageVO<EsOrderVO> esPageVO = new EsPageVO<>();
        HitsMetadata<EsOrderVO> hits = searchResponse.hits();
        List<EsOrderVO> productList = productListGetFromResponse(searchResponse);
        esPageVO.setList(productList);
        long total = 0;
        if (hits.total() != null) {
            total = hits.total().value();
        }
        int pages = total % pageDTO.getPageSize() == 0 ?
                ((int) total / pageDTO.getPageSize()) : ((int) total / pageDTO.getPageSize()) + 1;
        esPageVO.setPages(pages);
        return esPageVO;
    }

    private List<EsOrderVO> productListGetFromResponse(SearchResponse<EsOrderVO> searchResponse) {
        return getFromHints(searchResponse.hits().hits());
    }

    private List<EsOrderVO> getFromHints(List<Hit<EsOrderVO>> hits) {
        List<EsOrderVO> productList = new ArrayList<>();
        for (Hit<EsOrderVO> hit : hits) {
            EsOrderVO esOrderVO = JSON.parseObject(hit.toString(), EsOrderVO.class);
            productList.add(esOrderVO);
        }
        return productList;
    }

    private SearchRequest buildSearchRequest(EsPageDTO pageDTO, OrderSearchDTO orderSearchDTO) {
        SearchRequest.Builder searchBuilder = new SearchRequest.Builder();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        //过滤所需条件
        filterQueryIfNecessary(orderSearchDTO, boolBuilder);

        keyWordQueryIfNecessary(orderSearchDTO, boolBuilder);

        sort(searchBuilder, boolBuilder);

        //手动分页
        if (Objects.nonNull(pageDTO)) {
            searchBuilder.from((pageDTO.getPageNum() - 1) * pageDTO.getPageSize());
            searchBuilder.size(pageDTO.getPageSize());
        }

        searchBuilder.index(EsIndexEnum.ORDER.value());
        SearchRequest searchRequest = searchBuilder.build();
        log.info("构建的dsl语句是：{}", searchRequest.toString());
        return searchRequest;
    }

    private void sort(SearchRequest.Builder searchBuilder, BoolQuery.Builder boolBuilder) {
        searchBuilder.sort(s -> s
                .field(f -> f
                        .field("createTime")
                        .order(SortOrder.Desc)));
        searchBuilder.query(boolBuilder.build());
    }

    //在多个should 的外层套一个must保证should 至少有一个会进行匹配
    private void keyWordQueryIfNecessary(OrderSearchDTO orderSearchDTO, BoolQuery.Builder boolBuilder) {
        BoolQuery.Builder keywordQueryBuilder = new BoolQuery.Builder();
        if (orderSearchDTO.getOrderId() != null) {
            keywordQueryBuilder.should(q -> q
                    .match(m -> m
                            .field("orderId")
                            .query(orderSearchDTO.getOrderId())));
        }
        if (StrUtil.isNotBlank(orderSearchDTO.getShopName())) {
            keywordQueryBuilder.should(s -> s
                    .match(m -> m
                            .field("shopName")
                            .query(orderSearchDTO.getShopName())));
        }
        if (StrUtil.isNotBlank(orderSearchDTO.getSpuName())) {
            BoolQuery.Builder tmpBuilder = new BoolQuery.Builder();
            tmpBuilder.must(q -> q
                    .match(m -> m
                            .field("orderItems.spuName")
                            .query(orderSearchDTO.getSpuName())));
            NestedQuery nestedQuery = new NestedQuery.Builder()
                    .path("orderItems")
                    .query(q -> q.bool(tmpBuilder.build()))
                    .scoreMode(ChildScoreMode.None)
                    .build();
            keywordQueryBuilder.should(q -> q.nested(nestedQuery));
        }
        if (StrUtil.isNotBlank(orderSearchDTO.getConsignee())) {
            keywordQueryBuilder.should(s -> s
                    .match(m -> m
                            .field("consignee")
                            .query(orderSearchDTO.getConsignee())));
        }
        if (StrUtil.isNotBlank(orderSearchDTO.getMobile())) {
            keywordQueryBuilder.should(s -> s
                    .match(m -> m
                            .field("mobile")
                            .query(orderSearchDTO.getMobile())));
        }
        boolBuilder.must(keywordQueryBuilder.build());
    }

    private void filterQueryIfNecessary(OrderSearchDTO orderSearchDTO, BoolQuery.Builder boolBuilder) {
        if (Objects.nonNull(orderSearchDTO.getShopId())) {
            boolBuilder.filter(q -> q
                    .term(t -> t.field("shopId")
                            .value(orderSearchDTO.getShopId())));
        }
        if (Objects.nonNull(orderSearchDTO.getUserId())) {
            boolBuilder.filter(q -> q
                    .term(t -> t
                            .field("userId")
                            .value(orderSearchDTO.getUserId())));
        }
        if (Objects.nonNull(orderSearchDTO.getStatus()) && !Objects.equals(0, orderSearchDTO.getStatus())) {
            boolBuilder.filter(q -> q
                    .term(t -> t
                            .field("status")
                            .value(orderSearchDTO.getStatus())));
        }
        if (Objects.nonNull(orderSearchDTO.getIsPayed())) {
            boolBuilder.filter(q -> q
                    .term(t -> t
                            .field("isPayed")
                            .value(orderSearchDTO.getIsPayed())));
        }
        if (!Objects.nonNull(orderSearchDTO.getStartTime()) || !Objects.nonNull(orderSearchDTO.getEndTime())) {
            String createTime = "createTime";
            DateRangeQuery.Builder dateRangeBuilder = new DateRangeQuery.Builder();
            dateRangeBuilder.field(createTime);
            if (orderSearchDTO.getStartTime() != null) {
                dateRangeBuilder.gte(orderSearchDTO.getStartTime().toInstant().toString());
            }
            if (orderSearchDTO.getEndTime() != null) {
                dateRangeBuilder.lte(orderSearchDTO.getEndTime().toInstant().toString());
            }
            boolBuilder.filter(dateRangeBuilder.build()._toRangeQuery());
        }
        if (BooleanUtil.isTrue(orderSearchDTO.getDeliveryType())) {
            boolBuilder.filter(b -> b
                    .term(t -> t
                            .field("deliveryType")
                            .value(orderSearchDTO.getDeliveryType())));
        }
    }
}
