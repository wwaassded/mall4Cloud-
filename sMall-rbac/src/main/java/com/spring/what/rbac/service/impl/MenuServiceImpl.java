package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.rbac.model.Menu;
import com.spring.what.rbac.service.MenuService;
import com.spring.what.rbac.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【menu(菜单管理)】的数据库操作Service实现
* @createDate 2026-02-05 19:39:21
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




