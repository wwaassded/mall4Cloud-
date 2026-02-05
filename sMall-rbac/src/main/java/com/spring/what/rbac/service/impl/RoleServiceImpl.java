package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.model.Role;
import com.spring.what.rbac.service.RoleService;
import com.spring.what.rbac.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【role(角色)】的数据库操作Service实现
* @createDate 2026-02-05 19:39:21
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




