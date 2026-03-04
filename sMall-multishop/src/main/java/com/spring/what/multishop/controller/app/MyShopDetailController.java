package com.spring.what.multishop.controller.app;

import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.dto.ShopDetailDTO;
import com.spring.what.multishop.service.ShopDetailService;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RequestMapping(value = "/my_shop_detail")
@RestController("appMyShopDetailController")
@Tag(name = "app-我的店铺详情信息")
public class MyShopDetailController {

    @Resource
    private ShopDetailService shopDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建店铺", description = "创建店铺")
    public ServerResponseEntity<Void> create(@Valid @RequestBody ShopDetailDTO shopDetailDTO) {
        shopDetailService.createShop(shopDetailDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @Operation(summary = "获取我的店铺", description = "获取我的店铺")
    public ServerResponseEntity<ShopDetailVO> get() {
        Long shopId = AuthContext.get().getTenantId();
        if (shopId == null) {
            return ServerResponseEntity.success(null);
        }
        return ServerResponseEntity.success(shopDetailService.getMyShopDetailByShopId(shopId));
    }
}
