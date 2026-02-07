package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.rbac.mapper.MenuPermissionMapper;
import com.spring.what.rbac.model.Menu;
import com.spring.what.rbac.service.MenuService;
import com.spring.what.rbac.mapper.MenuMapper;
import com.spring.what.rbac.vo.MenuSimpleVO;
import com.spring.what.rbac.vo.MenuVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whatyi
 * @description 针对表【menu(菜单管理)】的数据库操作Service实现
 * @createDate 2026-02-05 19:39:21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    @Cacheable(cacheNames = CacheNames.MENU_LIST_KEY, key = "#sysType")
    public List<Menu> listBySystype(Integer sysType) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getBizType, sysType);
        return menuMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public MenuVO getByMenuId(Long menuId) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getMenuId, menuId);
        Menu menu = menuMapper.selectOne(lambdaQueryWrapper);
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.MENU_LIST_KEY, key = "#menu.bizType")
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.MENU_LIST_KEY, key = "#menu.bizType")
    public void updateMenu(Menu menu) {
        menuMapper.updateById(menu);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.MENU_LIST_KEY, key = "#sysType")
    public void removeMenuByIdAndSysType(Long menuId, Integer sysType) {
        LambdaQueryWrapper<Menu> lambdaWrapper = new LambdaQueryWrapper<>();
        lambdaWrapper.eq(Menu::getMenuId, menuId)
                .eq(Menu::getBizType, sysType);
        menuMapper.delete(lambdaWrapper);
    }

    @Override
    public List<MenuSimpleVO> listWithPermissions(Integer sysType) {
        return menuMapper.listWithPermissions(sysType);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.MENU_LIST_KEY, key = "#userId")
    public List<Long> listMenuIds(Long userId) {
        return menuMapper.listMenuIds(userId);
    }

}




