package com.spring.what.search.feign;

import cn.hutool.core.collection.CollUtil;
import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.dto.ProductSearchDTO;
import com.spring.what.api.feign.SearchSpuFeignClient;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.ProductSearchVO;
import com.spring.what.api.vo.search.SpuSearchVO;
import com.spring.what.common.constant.Constant;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.search.constant.EsConstant;
import com.spring.what.search.manager.ProductSearchManager;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class SearchSpuFeignClientController implements SearchSpuFeignClient {

    @Resource
    private ProductSearchManager productSearchManager;

    @Override
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> search(EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        return ServerResponseEntity.success(productSearchManager.simplePage(pageDTO, productSearchDTO));
    }

    @Override
    public ServerResponseEntity<List<SpuSearchVO>> getSpusBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        productSearchDTO.setSpuIds(spuIds);
        List<SpuSearchVO> list = productSearchManager.list(productSearchDTO);
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> spuPage(Integer pageNum, Integer pageSize, Long shopId) {
        EsPageDTO esPageDTO = new EsPageDTO();
        esPageDTO.setPageSize(pageSize);
        esPageDTO.setPageNum(pageNum);
        ProductSearchDTO productSearchDTO = new ProductSearchDTO();
        if (!Objects.equals(shopId, Constant.PLATFORM_SHOP_ID)) {
            productSearchDTO.setShopId(shopId);
        }
        return ServerResponseEntity.success(productSearchManager.simplePage(esPageDTO, productSearchDTO));
    }

    @Override
    public ServerResponseEntity<List<SpuSearchVO>> limitSizeListByShopIds(List<Long> shopIds, Integer size) {
        if (CollUtil.isEmpty(shopIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        return ServerResponseEntity.success(productSearchManager.limitSizeSearchByShopId(shopIds, size));
    }
}
