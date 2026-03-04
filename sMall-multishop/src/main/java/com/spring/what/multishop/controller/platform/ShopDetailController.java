package com.spring.what.multishop.controller.platform;

import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.dto.ShopDetailDTO;
import com.spring.what.multishop.dto.UpdateShopPasswordDTO;
import com.spring.what.multishop.service.ShopDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController("platformShopDetailController")
@RequestMapping("/platform/shop_detail")
@Tag(name = "platform-店铺信息")
public class ShopDetailController {

    @Resource
    private ShopDetailService shopDetailService;

    @GetMapping("/info")
    @Operation(summary = "店铺详情", description = "店铺详情")
    public ServerResponseEntity<ShopDetailVO> getInfo(@RequestParam Long shopId) {
        return ServerResponseEntity.success(shopDetailService.getMyShopDetailByShopId(shopId));
    }

    /**
     * 新建店铺
     */
    @PostMapping("/create_shop")
    @Operation(summary = "新建店铺", description = "新建店铺")
    public ServerResponseEntity<Void> createShop(@RequestBody ShopDetailDTO shopDetailDTO) {
        shopDetailService.createShop(shopDetailDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping("/reset_shop_password")
    @Operation(summary = "重置店铺密码", description = "重置店铺密码")
    public ServerResponseEntity<Void> resetShopPassword(@Valid @RequestBody UpdateShopPasswordDTO updateShopPasswordDTO) {
        shopDetailService.updateShopPassword(updateShopPasswordDTO);
        return ServerResponseEntity.success();
    }
}
