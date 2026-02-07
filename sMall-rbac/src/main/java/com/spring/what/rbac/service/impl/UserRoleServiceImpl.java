package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.model.UserRole;
import com.spring.what.rbac.service.UserRoleService;
import com.spring.what.rbac.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【user_role(用户与角色对应关系)】的数据库操作Service实现
* @createDate 2026-02-07 21:06:45
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




