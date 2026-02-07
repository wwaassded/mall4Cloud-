package com.spring.what.rbac.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.rbac.dto.RoleDTO;
import com.spring.what.rbac.model.Role;
import com.spring.what.rbac.service.RoleService;
import com.spring.what.rbac.vo.RoleVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/page/#{page}/#{size}")
    @Operation(summary = "分页获取角色列表", description = "分页获取角色列表")
    public ServerResponseEntity<IPage<RoleVO>> page(@PathVariable Integer page, @PathVariable Integer size) {
        Page<RoleVO> ipage = new Page<>(page, size);
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        Integer sysType = userInfoInTokenBO.getSysType();
        Long tenantId = userInfoInTokenBO.getTenantId();
        return ServerResponseEntity.success(roleService.page(ipage, sysType, tenantId));
    }

    @GetMapping("/list")
    @Operation(summary = "获取角色列表", description = "分页获取角色列表")
    public ServerResponseEntity<List<RoleVO>> list() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        Long tenantId = userInfoInTokenBO.getTenantId();
        Integer sysType = userInfoInTokenBO.getSysType();
        return ServerResponseEntity.success(roleService.getList(tenantId, sysType));
    }

    @GetMapping
    @Operation(summary = "获取角色", description = "根据roleId获取角色")
    public ServerResponseEntity<RoleVO> getByRoleId(@RequestParam Long roleId) {
        RoleVO roleVO = roleService.selectRoleVO(roleId);
        return ServerResponseEntity.success(roleVO);
    }

    @PostMapping
    @Operation(summary = "保存角色", description = "保存角色")
    public ServerResponseEntity<Void> save(@Valid @RequestBody RoleDTO roleDTO) {
        Role role = BeanUtil.map(roleDTO, Role.class);
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        role.setCreateUserId(userInfoInTokenBO.getUserId());
        role.setBizType(userInfoInTokenBO.getSysType());
        role.setTenantId(userInfoInTokenBO.getTenantId());
        roleService.saveRoleVO(role, roleDTO.getMenuIds(), roleDTO.getMenuPermissionIds());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色")
    public ServerResponseEntity<Void> update(@Valid @RequestBody RoleDTO roleDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        RoleVO DBRoleVO = roleService.selectRoleVO(roleDTO.getRoleId());
        if (!Objects.equals(DBRoleVO.getTenantId(), userInfoInTokenBO.getTenantId()) || !Objects.equals(DBRoleVO.getBizType(), userInfoInTokenBO.getSysType())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        Role role = BeanUtil.map(roleDTO, Role.class);
        role.setBizType(userInfoInTokenBO.getSysType());
        role.setTenantId(userInfoInTokenBO.getTenantId());
        roleService.updateRoleDTO(role, roleDTO.getMenuIds(), roleDTO.getMenuPermissionIds());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除角色", description = "根据角色id删除角色")
    public ServerResponseEntity<Void> delete(@RequestParam Long roleId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        RoleVO DBRoleVO = roleService.selectRoleVO(roleId);
        if (!Objects.equals(DBRoleVO.getTenantId(), userInfoInTokenBO.getTenantId()) || !Objects.equals(DBRoleVO.getBizType(), userInfoInTokenBO.getSysType())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        roleService.deletByRoleIdAndSystype(roleId, userInfoInTokenBO.getSysType());
        return ServerResponseEntity.success();
    }

}
