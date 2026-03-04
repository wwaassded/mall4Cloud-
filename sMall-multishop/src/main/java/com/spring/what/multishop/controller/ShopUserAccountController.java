package com.spring.what.multishop.controller;

import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.dto.ChangeAccountDTO;
import com.spring.what.multishop.service.ShopUserAccountService;
import com.spring.what.multishop.service.ShopUserService;
import com.spring.what.multishop.vo.ShopUserVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping(value = "/shop_user/account")
@RestController
@Tag(name = "店铺用户账号信息")
public class ShopUserAccountController {

    @Resource
    private ShopUserService shopUserService;

    @Resource
    private ShopUserAccountService shopUserAccountService;

    @GetMapping
    @Operation(summary = "获取账号信息", description = "获取账号信息")
    public ServerResponseEntity<AuthAccountVO> getAccount(Long shopUserId) {
        AuthAccountVO authAccountVO = shopUserAccountService.getAccount(shopUserId, AuthContext.get().getSysType());
        return ServerResponseEntity.success(authAccountVO);
    }


    @PostMapping
    @Operation(summary = "添加账号", description = "添加账号")
    public ServerResponseEntity<Void> addAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        ShopUserVO shopUserVO = shopUserService.getByShopUserId(changeAccountDTO.getUserId());
        if (Objects.isNull(shopUserVO)) {
            throw new Mall4cloudException("无法获取用户的信息");
        }
        if (!Objects.equals(shopUserVO.getShopId(), AuthContext.get().getTenantId())) {
            throw new Mall4cloudException(ResponseEnum.UNAUTHORIZED);
        }
        if (Objects.equals(shopUserVO.getHasAccount(), 1)) {
            throw new Mall4cloudException("用户已经拥有账户无需重复创建");
        }
        return shopUserAccountService.addAccount(changeAccountDTO);
    }

    @PutMapping
    @Operation(summary = "修改账号", description = "修改账号")
    public ServerResponseEntity<Void> updateAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        ShopUserVO shopUserVO = shopUserService.getByShopUserId(changeAccountDTO.getUserId());
        if (shopUserVO == null || Objects.equals(shopUserVO.getHasAccount(), 0)) {
            throw new Mall4cloudException("无法获取用户的信息");
        }
        if (!Objects.equals(shopUserVO.getShopId(), AuthContext.get().getTenantId())) {
            throw new Mall4cloudException(ResponseEnum.UNAUTHORIZED);
        }
        return shopUserAccountService.update(changeAccountDTO);
    }
}