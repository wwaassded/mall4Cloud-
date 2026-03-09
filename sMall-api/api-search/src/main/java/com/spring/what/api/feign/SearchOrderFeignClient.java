package com.spring.what.api.feign;

import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.EsOrderVO;
import com.spring.what.common.dto.OrderSearchDTO;
import com.spring.what.common.feign.FeignInsideAuthConfig;
import com.spring.what.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 订单搜索
 *
 * @author FrozenWatermelon
 * @date 2021/02/05
 */
@FeignClient(value = "mall4cloud-search", contextId = "searchOrder")
public interface SearchOrderFeignClient {


    /**
     * 订单搜索
     *
     * @param orderSearch 订单搜索参数
     * @return 订单列表
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchOrder/getOrderPage")
    ServerResponseEntity<EsPageVO<EsOrderVO>> getOrderPage(@RequestBody OrderSearchDTO orderSearch);

}
