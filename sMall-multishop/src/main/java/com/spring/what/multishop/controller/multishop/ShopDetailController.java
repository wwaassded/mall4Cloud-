package com.spring.what.multishop.controller.multishop;

import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.service.ShopDetailService;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/m/shop_detail")
@RestController("multishopShopDetailController")
@Tag(name = "multishop-店铺详情信息")
public class ShopDetailController {

    @Resource
    private ShopDetailService shopDetailService;

    @GetMapping("/info")
    @Operation(summary = "获取店铺详情信息", description = "获取店铺详情信息")
    public ServerResponseEntity<ShopDetailVO> info() {
        Long shopId = AuthContext.get().getTenantId();
        ShopDetailVO myShopDetailByShopId = shopDetailService.getMyShopDetailByShopId(shopId);
        return ServerResponseEntity.success(myShopDetailByShopId);
    }
}