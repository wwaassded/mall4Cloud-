package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.model.MenuPermission;
import com.spring.what.rbac.service.MenuPermissionService;
import com.spring.what.rbac.mapper.MenuPermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【menu_permission(菜单资源)】的数据库操作Service实现
* @createDate 2026-02-05 19:39:21
*/
@Service
public class MenuPermissionServiceImpl extends ServiceImpl<MenuPermissionMapper, MenuPermission>
    implements MenuPermissionService{

}




