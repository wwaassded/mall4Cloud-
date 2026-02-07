package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.model.RoleMenu;
import com.spring.what.rbac.service.RoleMenuService;
import com.spring.what.rbac.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【role_menu(角色与菜单对应关系)】的数据库操作Service实现
* @createDate 2026-02-07 20:44:47
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




