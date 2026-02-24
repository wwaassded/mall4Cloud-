package com.spring.what.rbac.mapper;

import com.spring.what.rbac.model.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【user_role(用户与角色对应关系)】的数据库操作Mapper
 * @createDate 2026-02-07 21:06:45
 * @Entity com.spring.what.rbac.model.UserRole
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void saveByUserIdAndSysType(@Param("userid") @NotNull(message = "userId not null") Long userId, @Param("roleIdList") @NotEmpty(message = "userId not null") List<Long> roleIds);

    List<Long> getRoleIds(@Param("userid") Long userId);
}




