package com.spring.what.rbac.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.rbac.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.rbac.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author whatyi
 * @description 针对表【role(角色)】的数据库操作Mapper
 * @createDate 2026-02-05 19:39:21
 * @Entity com.spring.what.rbac.model.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    IPage<RoleVO> page(Page<RoleVO> ipage, @Param("systype") Integer sysType, @Param("tenantId") Long tenantId);

    RoleVO selectRoleVO(@Param("roleid") Long roleId);
}




