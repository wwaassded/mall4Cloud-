package com.spring.what.rbac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.rbac.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.rbac.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【role(角色)】的数据库操作Service
 * @createDate 2026-02-05 19:39:21
 */
public interface RoleService extends IService<Role> {

    IPage<RoleVO> page(Page<RoleVO> ipage, Integer sysType, Long tenantId);

    List<RoleVO> getList(@Param("tenantid") Long tenantId, @Param("systype") Integer sysType);

    RoleVO selectRoleVO(Long roleId);

    void saveRoleVO(Role role, List<Long> menuIds, List<Long> menuPermissionIds);

    void updateRoleDTO(Role role, List<Long> menuIds, List<Long> menuPermissionIds);

    void deletByRoleIdAndSystype(Long roleId, Integer sysType);
}
