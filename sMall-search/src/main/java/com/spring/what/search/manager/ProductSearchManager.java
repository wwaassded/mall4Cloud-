package com.spring.what.search.manager;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson2.JSON;
import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.dto.ProductSearchDTO;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.ProductSearchVO;
import com.spring.what.api.vo.search.SpuSearchVO;
import com.spring.what.common.constant.StatusEnum;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.search.constant.EsConstant;
import com.spring.what.search.constant.EsIndexEnum;
import com.spring.what.search.constant.SearchTypeEnum;
import com.spring.what.search.vo.SpuAdminVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class ProductSearchManager {
    private static final Logger log = LoggerFactory.getLogger(ProductSearchManager.class);

    @Resource
    private ElasticsearchClient myElasticSearchClient;

    public EsPageVO<ProductSearchVO> page(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSpuStatus(StatusEnum.ENABLE.value());
        productSearchDTO.setSearchType(SearchTypeEnum.APP.value());
        SearchResponse<ProductSearchVO> response = pageSearchResult(pageDTO, productSearchDTO, Boolean.TRUE);
        return buildSearchResult(pageDTO, response);
    }

    public EsPageVO<ProductSearchVO> simplePage(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSpuStatus(StatusEnum.ENABLE.value());
        productSearchDTO.setSearchType(SearchTypeEnum.APP.value());
        SearchResponse<ProductSearchVO> response = pageSearchResult(pageDTO, productSearchDTO, Boolean.FALSE);
        return buildSearchResult(pageDTO, response);
    }

    public List<SpuSearchVO> list(ProductSearchDTO productSearchDTO) {
        SearchRequest searchRequest = buildSearchRequest(null, productSearchDTO, Boolean.TRUE);
        SearchResponse<SpuSearchVO> searchResponse = null;
        try {
            searchResponse = myElasticSearchClient.search(searchRequest, SpuSearchVO.class);
        } catch (IOException e) {
            log.error(e.toString());
            throw new Mall4cloudException("服务器发生了点小错误", e);
        }
        return getSpuSearchVOFromHits(searchResponse.hits());
    }

    private List<SpuSearchVO> getSpuSearchVOFromHits(HitsMetadata<SpuSearchVO> hits) {
        return null;
    }

    private SearchResponse<ProductSearchVO> pageSearchResult(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO, Boolean aTrue) {
        SearchRequest searchRequest = buildSearchRequest(pageDTO, productSearchDTO, aTrue);
        SearchResponse<ProductSearchVO> response;
        try {
            response = myElasticSearchClient.search(searchRequest, ProductSearchVO.class);
        } catch (IOException e) {
            log.error(e.toString());
            throw new Mall4cloudException("服务器出了点小差请稍后重试", e);
        }
        return response;
    }

    private SearchRequest buildSearchRequest(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO, Boolean aTrue) {
        SearchRequest.Builder builder = new SearchRequest.Builder();
        builder.source(s -> s
                .filter(f -> f
                        .includes(Arrays.stream(EsConstant.FETCH_SOURCE).toList())));
        //TODO: do something
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        return builder.index(EsIndexEnum.PRODUCT.value()).build();
    }

    private EsPageVO<ProductSearchVO> buildSearchResult(EsPageDTO pageDTO, SearchResponse<ProductSearchVO> response) {
        EsPageVO<ProductSearchVO> pageVO = new EsPageVO<>();
        HitsMetadata<ProductSearchVO> hits = response.hits();
        List<ProductSearchVO> productSearchVOS = getFromHits(hits);
        pageVO.setList(productSearchVOS);
        long total = 0;
        if (hits.total() != null) {
            total = hits.total().value();
        }
        pageVO.setTotal(total);
        int pages = total % pageDTO.getPageSize() == 0 ? ((int) total / pageDTO.getPageSize()) : ((int) total / pageDTO.getPageSize()) + 1;
        pageVO.setPages(pages);
        return pageVO;
    }

    private List<ProductSearchVO> getFromHits(HitsMetadata<ProductSearchVO> hits) {
        List<Hit<ProductSearchVO>> hitList = hits.hits();
        List<ProductSearchVO> list = new ArrayList<>();
        for (Hit<ProductSearchVO> hit : hitList) {
            ProductSearchVO productSearchVO = JSON.parseObject(hit.toString(), ProductSearchVO.class);
            list.add(productSearchVO);
        }
        return list;
    }

    public List<SpuSearchVO> limitSizeSearchByShopId(List<Long> shopIds, Integer size) {
        SearchRequest.Builder searchBuilder = new SearchRequest.Builder().index(EsIndexEnum.PRODUCT.value());
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        boolBuilder.filter(f -> f.terms(t -> t
                .field("shopId")
                .terms(v -> v
                        .value(shopIds.stream().map(FieldValue::of).toList())
                )));
        searchBuilder.query(q -> q.bool(boolBuilder.build()));
        Aggregation.Builder aggregationBuilder = getBuilder(size);
        searchBuilder.aggregations(EsConstant.SHOP_COUPON, aggregationBuilder.build()).size(0);
        List<SpuSearchVO> spuSearchVOS = null;
        SearchResponse<SpuSearchVO> response;
        try {
            response = myElasticSearchClient.search(searchBuilder.build(), SpuSearchVO.class);
            spuSearchVOS = getFromResponse(response);
        } catch (IOException e) {
            throw new Mall4cloudException("", e);
        }
        return spuSearchVOS;
    }

    private List<SpuSearchVO> getFromResponse(SearchResponse<SpuSearchVO> response) {
        Aggregate couponAggregate = response.aggregations().get(EsConstant.SHOP_COUPON);
        List<SpuSearchVO> spuSearchVOS = new ArrayList<>();
        if (Objects.nonNull(couponAggregate)) {
            Buckets<LongTermsBucket> buckets = couponAggregate.lterms().buckets();
            for (LongTermsBucket bucket : buckets.array()) {
                Aggregate topHitsAggregation = bucket.aggregations().get(EsConstant.TOP_HITS_DATA);
                spuSearchVOS.addAll(getSpuListFromResponse(topHitsAggregation.topHits().hits().hits()));
            }
        }
        return spuSearchVOS;
    }

    private Collection<? extends SpuSearchVO> getSpuListFromResponse(List<Hit<JsonData>> hits) {
        List<SpuSearchVO> spus = new ArrayList<>();
        for (var hit : hits) {
            SpuSearchVO spuSearchVO = JSON.parseObject(hit.toString(), SpuSearchVO.class);
            spus.add(spuSearchVO);
        }
        return spus;
    }

    private static Aggregation.@NonNull Builder getBuilder(Integer size) {
        Aggregation.Builder aggregationBuilder = new Aggregation.Builder();
        String[] include = {EsConstant.SPU_NAME, EsConstant.MAIN_IMG_URL, EsConstant.SHOP_ID, EsConstant.SPU_ID, EsConstant.PRICE_FEE};
        aggregationBuilder.terms(t -> t.field(EsConstant.SHOP_ID));
        aggregationBuilder.aggregations(EsConstant.TOP_HITS_DATA, a -> a
                .topHits(v -> v
                        .sort(s -> s
                                .field(f -> f
                                        .field(EsConstant.SALE_NUM)
                                        .order(SortOrder.Desc)))
                        .size(size)
                        .source(s -> s
                                .filter(sf -> sf
                                        .includes(Arrays.stream(include).toList())))
                ));
        return aggregationBuilder;
    }

    public EsPageVO<SpuAdminVO> adminPage(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        return null;
    }
}
