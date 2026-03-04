package com.spring.what.multishop.controller.app;

import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.service.ShopDetailService;
import com.spring.what.multishop.vo.ShopHeadInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/ua/shop_detail")
@RestController("appShopDetailController")
@Tag(name = "app-店铺详情信息")
public class ShopDetailController {

    @Resource
    private ShopDetailService shopDetailService;

    @GetMapping("/check_shop_name")
    @Operation(summary = "验证店铺名称是否重名", description = "验证店铺名称是否重名")
    public ServerResponseEntity<Boolean> checkShopName(@RequestParam("shopName") String shopName) {
        return ServerResponseEntity.success(shopDetailService.checkShopName(shopName));
    }

    @GetMapping("/head_info")
    @Operation(summary = "店铺头部信息", description = "店铺头部信息")
    public ServerResponseEntity<ShopHeadInfoVO> getShopHeadInfo(Long shopId) {
        return ServerResponseEntity.success(shopDetailService.getShopHeadInfo(shopId));
    }
}
