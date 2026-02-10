package com.spring.what.rbac.controller;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.common.util.BooleanUtil;
import com.spring.what.rbac.dto.MenuPermissionDTO;
import com.spring.what.rbac.model.MenuPermission;
import com.spring.what.rbac.service.MenuPermissionService;
import com.spring.what.rbac.vo.MenuPermissionVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu_permission")
public class MenuPermissionController {

    @Resource
    private MenuPermissionService menuPermissionService;

    @GetMapping("/list_by_menu")
    @Operation(summary = "获取菜单资源列表", description = "分页获取菜单资源列表")
    public ServerResponseEntity<List<MenuPermissionVO>> listByMenuId(Long menuId) {
        return ServerResponseEntity.success(menuPermissionService.listByMenuId(menuId));
    }

    @GetMapping
    @Operation(summary = "获取菜单资源", description = "根据menuPermissionId获取菜单资源")
    public ServerResponseEntity<MenuPermissionVO> getByMenuPermissionId(@RequestParam Long menuPermissionId) {
        return ServerResponseEntity.success(menuPermissionService.getMenuPermissionId(menuPermissionId));
    }

    @PostMapping
    @Operation(summary = "保存菜单资源", description = "保存菜单资源")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO) {
        MenuPermission menuPermission = BeanUtil.map(menuPermissionDTO, MenuPermission.class);
        menuPermission.setMenuPermissionId(null);
        menuPermission.setBizType(AuthContext.get().getSysType());
        return menuPermissionService.saveMenuPermission(menuPermission);
    }

    @PutMapping
    @Operation(summary = "更新菜单资源", description = "更新菜单资源")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO) {
        MenuPermission menuPermission = BeanUtil.map(menuPermissionDTO, MenuPermission.class);
        menuPermission.setBizType(AuthContext.get().getSysType());
        return menuPermissionService.updateMenuPermission(menuPermission);
    }

    @DeleteMapping
    @Operation(summary = "删除菜单资源", description = "根据菜单资源id删除菜单资源")
    public ServerResponseEntity<Void> delete(@RequestParam Long menuPermissionId) {
        menuPermissionService.deleteMenuPermission(menuPermissionId, AuthContext.get().getSysType());
        return ServerResponseEntity.success();
    }

    @GetMapping(value = "/list")
    @Operation(summary = "获取当前用户拥有的权限", description = "当前用户所拥有的所有权限，精确到按钮，实际上element admin里面的roles就可以理解成权限")
    public ServerResponseEntity<List<String>> permissions() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        return ServerResponseEntity.success(menuPermissionService.permissions(userInfoInTokenBO.getUserId(), userInfoInTokenBO.getSysType(), BooleanUtil.isTrue(userInfoInTokenBO.getIsAdmin())));
    }
}
