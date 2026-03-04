package com.spring.what.multishop.controller.multishop;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.dto.ShopUserDTO;
import com.spring.what.multishop.service.ShopDetailService;
import com.spring.what.multishop.service.ShopUserService;
import com.spring.what.multishop.vo.ShopUserSimpleVO;
import com.spring.what.multishop.vo.ShopUserVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RequestMapping(value = "/m/shop_user")
@RestController("multishopShopUserController")
@Tag(name = "店铺用户信息")
public class ShopUserController {

    @Resource
    private ShopUserService shopUserService;

    @Resource
    private ShopDetailService shopDetailService;


    @GetMapping("/info")
    @Operation(summary = "登陆店铺用户信息", description = "获取当前登陆店铺用户的用户信息")
    public ServerResponseEntity<ShopUserSimpleVO> info() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        ShopUserSimpleVO shopUserSimpleVO = new ShopUserSimpleVO();
        shopUserSimpleVO.setIsAdmin(userInfoInTokenBO.getIsAdmin());
        ShopDetailVO myShopDetailByShopId = shopDetailService.getMyShopDetailByShopId(userInfoInTokenBO.getTenantId());
        shopUserSimpleVO.setNickName(myShopDetailByShopId.getShopName());
        shopUserSimpleVO.setAvatar(myShopDetailByShopId.getShopLogo());
        return ServerResponseEntity.success(shopUserSimpleVO);
    }

    @GetMapping
    @Operation(summary = "获取店铺用户信息", description = "根据用户id获取店铺用户信息")
    public ServerResponseEntity<ShopUserVO> detail(@RequestParam Long shopUserId) {
        return ServerResponseEntity.success(shopUserService.getByShopUserId(shopUserId));
    }

    @PostMapping
    @Operation(summary = "保存店铺用户信息", description = "保存店铺用户信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ShopUserDTO shopUserDTO) {
        shopUserService.saveShopUserDTO(shopUserDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新店铺用户信息", description = "更新店铺用户信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ShopUserDTO shopUserDTO) {
        shopUserService.updateShopUserDTO(shopUserDTO);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除店铺用户信息", description = "根据店铺用户id删除店铺用户信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long shopUserId) {
        ShopUserVO dbShopUserVO = shopUserService.getByShopUserId(shopUserId);
        if (dbShopUserVO == null || !Objects.equals(dbShopUserVO.getShopId(), AuthContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        shopUserService.deleteByUserId(shopUserId);
        return ServerResponseEntity.success();
    }
}
