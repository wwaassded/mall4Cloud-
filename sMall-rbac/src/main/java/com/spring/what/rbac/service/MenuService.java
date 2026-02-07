package com.spring.what.rbac.service;

import com.spring.what.rbac.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.rbac.vo.MenuSimpleVO;
import com.spring.what.rbac.vo.MenuVO;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【menu(菜单管理)】的数据库操作Service
 * @createDate 2026-02-05 19:39:21
 */
public interface MenuService extends IService<Menu> {

    List<Menu> listBySystype(Integer sysType);

    MenuVO getByMenuId(Long menuId);

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenuByIdAndSysType(Long menuId, Integer sysType);

    List<MenuSimpleVO> listWithPermissions(Integer sysType);

    List<Long> listMenuIds(Long userId);
}
