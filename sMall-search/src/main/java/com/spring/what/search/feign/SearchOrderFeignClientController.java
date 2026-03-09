package com.spring.what.search.feign;

import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.feign.SearchOrderFeignClient;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.EsOrderVO;
import com.spring.what.common.dto.OrderSearchDTO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.search.manager.OrderSearchManager;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchOrderFeignClientController implements SearchOrderFeignClient {

    @Resource
    private OrderSearchManager orderSearchManager;

    @Override
    public ServerResponseEntity<EsPageVO<EsOrderVO>> getOrderPage(OrderSearchDTO orderSearch) {
        EsPageDTO esPageDTO = new EsPageDTO();
        esPageDTO.setPageNum(orderSearch.getPageNum());
        esPageDTO.setPageSize(orderSearch.getPageSize());
        return ServerResponseEntity.success(orderSearchManager.pageSearchResult(esPageDTO, orderSearch));
    }
}
