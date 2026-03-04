package com.spring.what.multishop.feign;

import com.spring.what.api.multishop.bo.EsShopDetailBO;
import com.spring.what.api.multishop.feign.ShopDetailFeignClient;
import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.multishop.service.ShopDetailService;
import jakarta.annotation.Resource;

import java.util.List;

public class ShopDetailFeignClientController implements ShopDetailFeignClient {

    @Resource
    private ShopDetailService shopDetailService;

    @Override
    public ServerResponseEntity<String> getShopNameByShopId(Long shopId) {
        ShopDetailVO shopDetailVO = shopDetailService.getMyShopDetailByShopId(shopId);
        if (shopDetailVO == null) {
            return ServerResponseEntity.success("");
        }
        return ServerResponseEntity.success(shopDetailVO.getShopName());
    }

    @Override
    public ServerResponseEntity<EsShopDetailBO> getShopByShopId(Long shopId) {
        ShopDetailVO shopDetailVO = shopDetailService.getMyShopDetailByShopId(shopId);
        if (shopDetailVO == null) {
            return ServerResponseEntity.success(new EsShopDetailBO());
        }
        EsShopDetailBO esShopDetailBO = BeanUtil.map(shopDetailVO, EsShopDetailBO.class);
        return ServerResponseEntity.success(esShopDetailBO);
    }

    @Override
    public ServerResponseEntity<List<ShopDetailVO>> listByShopIds(List<Long> shopIds) {
        return ServerResponseEntity.success(BeanUtil.mapAsList(shopDetailService.listByShopIds(shopIds), ShopDetailVO.class));
    }

    @Override
    public ServerResponseEntity<EsShopDetailBO> shopExtensionData(Long shopId) {
        ShopDetailVO shopDetailVO = shopDetailService.getMyShopDetailByShopId(shopId);
        if (shopDetailVO == null) {
            return ServerResponseEntity.success(new EsShopDetailBO());
        }
        EsShopDetailBO esShopDetailBO = BeanUtil.map(shopDetailVO, EsShopDetailBO.class);
        return ServerResponseEntity.success(esShopDetailBO);
    }

    @Override
    public ServerResponseEntity<List<ShopDetailVO>> getShopDetailByShopIdAndShopName(List<Long> shopIds, String shopName) {
        return ServerResponseEntity.success(BeanUtil.mapAsList(shopDetailService.listByShopIdsAndShopName(shopIds, shopName), ShopDetailVO.class));
    }
}
