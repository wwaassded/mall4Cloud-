package com.spring.what.rbac.controller;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.common.util.BooleanUtil;
import com.spring.what.rbac.dto.MenuDTO;
import com.spring.what.rbac.model.Menu;
import com.spring.what.rbac.service.MenuService;
import com.spring.what.rbac.vo.MenuSimpleVO;
import com.spring.what.rbac.vo.MenuVO;
import com.spring.what.rbac.vo.RouteMetaVO;
import com.spring.what.rbac.vo.RouteVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping(value = "/route")
    @Operation(summary = "路由菜单", description = "获取当前登陆用户可用的路由菜单列表")
    public ServerResponseEntity<List<RouteVO>> route(Integer sysType) {
        sysType = Objects.isNull(sysType) ? AuthContext.get().getSysType() : sysType;
        List<Menu> menus = menuService.listBySystype(sysType);
        List<RouteVO> routeVOS = new ArrayList<>();
        for (Menu menu : menus) {
            RouteVO route = new RouteVO();
            route.setAlwaysShow(BooleanUtil.isTrue(menu.getAlwaysShow()));
            route.setComponent(menu.getComponent());
            route.setHidden(BooleanUtil.isTrue(menu.getHidden()));
            route.setName(menu.getName());
            route.setPath(menu.getPath());
            route.setRedirect(menu.getRedirect());
            route.setId(menu.getMenuId());
            route.setParentId(menu.getParentId());
            route.setSeq(menu.getSeq());
            RouteMetaVO meta = new RouteMetaVO();
            meta.setActiveMenu(menu.getActiveMenu());
            meta.setAffix(BooleanUtil.isTrue(menu.getAffix()));
            meta.setBreadcrumb(BooleanUtil.isTrue(menu.getBreadcrumb()));
            meta.setIcon(menu.getIcon());
            meta.setNoCache(BooleanUtil.isTrue(menu.getNoCache()));
            meta.setTitle(menu.getTitle());
            meta.setRoles(Collections.singletonList(menu.getPermission()));
            route.setMeta(meta);
            route.setMeta(meta);
            routeVOS.add(route);
        }
        return ServerResponseEntity.success(routeVOS);
    }

    @GetMapping
    @Operation(summary = "获取菜单管理", description = "根据menuId获取菜单管理")
    public ServerResponseEntity<MenuVO> getByMenuId(@RequestParam Long menuId) {
        return ServerResponseEntity.success(menuService.getByMenuId(menuId));
    }

    @PostMapping
    @Operation(summary = "保存菜单管理", description = "保存菜单管理")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MenuDTO menuDTO) {
        Menu menu = checkAndGenerate(menuDTO);
        menu.setMenuId(null);
        menuService.saveMenu(menu);
        return ServerResponseEntity.success();
    }

    private Menu checkAndGenerate(@RequestBody @Valid MenuDTO menuDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        if (Objects.equals(userInfoInTokenBO.getTenantId(), 0L)) {
            throw new Mall4cloudException("无权限操作！");
        }
        Menu menu = BeanUtil.map(menuDTO, Menu.class);
        menu.setBizType(menuDTO.getSysType());
        if (Objects.isNull(menuDTO.getSysType())) {
            menu.setBizType(userInfoInTokenBO.getSysType());
        }
        return menu;
    }

    @PutMapping
    @Operation(summary = "更新菜单管理", description = "更新菜单管理")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MenuDTO menuDTO) {
        Menu menu = checkAndGenerate(menuDTO);
        menuService.updateMenu(menu);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除菜单管理", description = "根据菜单管理id删除菜单管理")
    public ServerResponseEntity<Void> delete(@RequestParam("menuId") Long menuId, @RequestParam("sysType") Integer sysType) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        if (Objects.equals(userInfoInTokenBO.getTenantId(), 0L)) {
            throw new Mall4cloudException("无权限操作！");
        }
        sysType = Objects.isNull(sysType) ? userInfoInTokenBO.getSysType() : sysType;
        menuService.removeMenuByIdAndSysType(menuId, sysType);
        return ServerResponseEntity.success();
    }

    @GetMapping(value = "/list_with_permissions")
    @Operation(summary = "菜单列表和按钮列表", description = "根据系统类型获取该系统的菜单列表 + 菜单下的权限列表")
    public ServerResponseEntity<List<MenuSimpleVO>> listWithPermissions() {
        Integer sysType = AuthContext.get().getSysType();
        return ServerResponseEntity.success(menuService.listWithPermissions(sysType));
    }

    @GetMapping(value = "/list_menu_ids")
    @Operation(summary = "获取当前用户可见的菜单ids", description = "获取当前用户可见的菜单id")
    public ServerResponseEntity<List<Long>> listMenuIds() {
        Long userId = AuthContext.get().getUserId();
        return ServerResponseEntity.success(menuService.listMenuIds(userId));
    }
}
