package com.spring.what.rbac.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.rbac.dto.MenuPermissionDTO;
import com.spring.what.rbac.service.MenuPermissionService;
import com.spring.what.rbac.vo.MenuPermissionVO;
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

    }

    @GetMapping
    @Operation(summary = "获取菜单资源", description = "根据menuPermissionId获取菜单资源")
    public ServerResponseEntity<MenuPermissionVO> getByMenuPermissionId(@RequestParam Long menuPermissionId) {
    }

    @PostMapping
    @Operation(summary = "保存菜单资源", description = "保存菜单资源")
    public ServerResponseEntity<Void> save(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO) {

    }

    @PutMapping
    @Operation(summary = "更新菜单资源", description = "更新菜单资源")
    public ServerResponseEntity<Void> update(@Valid @RequestBody MenuPermissionDTO menuPermissionDTO) {

    }

    @DeleteMapping
    @Operation(summary = "删除菜单资源", description = "根据菜单资源id删除菜单资源")
    public ServerResponseEntity<Void> delete(@RequestParam Long menuPermissionId) {

    }

    @GetMapping(value = "/list")
    @Operation(summary = "获取当前用户拥有的权限", description = "当前用户所拥有的所有权限，精确到按钮，实际上element admin里面的roles就可以理解成权限")
    public ServerResponseEntity<List<String>> permissions() {

    }

    @GetMapping(value = "/page/#{limit}/#{size}")
    @Operation(summary = "获取当前用户拥有的权限", description = "当前用户所拥有的所有权限，精确到按钮，实际上element admin里面的roles就可以理解成权限")
    public ServerResponseEntity<IPage<MenuPermissionVO>> pagePermissions(@PathVariable Integer limit, @PathVariable Integer size) {

    }
}
