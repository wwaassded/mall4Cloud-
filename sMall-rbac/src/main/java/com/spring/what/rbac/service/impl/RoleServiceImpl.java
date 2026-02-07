package com.spring.what.rbac.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.mapper.RoleMenuMapper;
import com.spring.what.rbac.mapper.UserRoleMapper;
import com.spring.what.rbac.model.Role;
import com.spring.what.rbac.model.RoleMenu;
import com.spring.what.rbac.model.UserRole;
import com.spring.what.rbac.service.RoleService;
import com.spring.what.rbac.mapper.RoleMapper;
import com.spring.what.rbac.vo.RoleVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whatyi
 * @description 针对表【role(角色)】的数据库操作Service实现
 * @createDate 2026-02-05 19:39:21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    //FIXME 可以修改逻辑完成自行控制的分页逻辑 否则会导致多次sql查询降低api的执行效率
    @Override
    public IPage<RoleVO> page(Page<RoleVO> ipage, Integer sysType, Long tenantId) {
        return roleMapper.page(ipage, sysType, tenantId);
    }

    @Override
    public List<RoleVO> getList(Long tenantId, Integer sysType) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getTenantId, tenantId)
                .eq(Role::getBizType, sysType);
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
        List<RoleVO> res = new ArrayList<>(roles.size());
        for (var role : roles) {
            RoleVO roleVO = new RoleVO();
            roleVO.setRoleId(role.getRoleId());
            roleVO.setRoleName(role.getRoleName());
            roleVO.setRemark(role.getRemark());
            roleVO.setBizType(sysType);
            roleVO.setTenantId(tenantId);
            roleVO.setCreateUserId(role.getCreateUserId());
            res.add(roleVO);
        }
        return res;
    }

    @Override
    public RoleVO selectRoleVO(Long roleId) {
        return roleMapper.selectRoleVO(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleVO(Role role, List<Long> menuIds, List<Long> menuPermissionIds) {
        roleMapper.insert(role);
        insertPermissions(role.getRoleId(), menuIds, menuPermissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleDTO(Role role, List<Long> menuIds, List<Long> menuPermissionIds) {
        roleMapper.updateById(role);
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(lambdaQueryWrapper);
        insertPermissions(role.getRoleId(), menuIds, menuPermissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletByRoleIdAndSystype(Long roleId, Integer sysType) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleId, roleId)
                .eq(Role::getBizType, sysType);
        roleMapper.delete(lambdaQueryWrapper);
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(roleMenuLambdaQueryWrapper);
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getRoleId, roleId);
        userRoleMapper.delete(userRoleLambdaQueryWrapper);
    }

    private void insertPermissions(Long roleId, List<Long> menuIds, List<Long> menuPermissionIds) {
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<RoleMenu> roleMenus = menuIds.stream().map(id -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(id);
                return roleMenu;
            }).toList();
            roleMenuMapper.insert(roleMenus);
        }

        if (CollectionUtil.isNotEmpty(menuPermissionIds)) {
            List<RoleMenu> roleMenus = menuPermissionIds.stream().map(id -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuPermissionId(id);
                roleMenu.setRoleId(roleId);
                return roleMenu;
            }).toList();
            roleMenuMapper.insert(roleMenus);
        }
    }
}




