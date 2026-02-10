package com.spring.what.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.rbac.bo.UriPermissionBO;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.rbac.model.MenuPermission;
import com.spring.what.rbac.service.MenuPermissionService;
import com.spring.what.rbac.mapper.MenuPermissionMapper;
import com.spring.what.rbac.vo.MenuPermissionVO;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【menu_permission(菜单资源)】的数据库操作Service实现
 * @createDate 2026-02-05 19:39:21
 */
@Service
public class MenuPermissionServiceImpl extends ServiceImpl<MenuPermissionMapper, MenuPermission>
        implements MenuPermissionService {

    @Resource
    private MenuPermissionMapper menuPermissionMapper;

    @Override
    public List<MenuPermissionVO> listByMenuId(Long menuId) {
        return menuPermissionMapper.listByMenuId(menuId);
    }

    @Override
    public MenuPermissionVO getMenuPermissionId(Long menuPermissionId) {
        return menuPermissionMapper.getMenuPermissionId(menuPermissionId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.URI_PERMISSION_KEY, key = "#menuPermission.bizType")
    public ServerResponseEntity<Void> saveMenuPermission(MenuPermission menuPermission) {
        LambdaQueryWrapper<MenuPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MenuPermission::getBizType, menuPermission.getBizType())
                .eq(MenuPermission::getPermission, menuPermission.getPermission());
        MenuPermission dbMenuPermission = menuPermissionMapper.selectOne(lambdaQueryWrapper);
        if (!Objects.isNull(dbMenuPermission)) {
            return ServerResponseEntity.showFail("相对应的权限已经存在了");
        }
        menuPermissionMapper.insert(menuPermission);
        return ServerResponseEntity.success();
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.URI_PERMISSION_KEY, key = "#menuPermission.bizType")
    public ServerResponseEntity<Void> updateMenuPermission(MenuPermission menuPermission) {
        LambdaQueryWrapper<MenuPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MenuPermission::getBizType, menuPermission.getBizType())
                .eq(MenuPermission::getPermission, menuPermission.getPermission());
        MenuPermission dbMenuPermission = menuPermissionMapper.selectOne(lambdaQueryWrapper);
        if (!Objects.isNull(dbMenuPermission) && Objects.equals(dbMenuPermission.getMenuPermissionId(), menuPermission.getMenuPermissionId())) {
            return ServerResponseEntity.showFail("相对应的权限已经存在了");
        }
        menuPermissionMapper.updateById(menuPermission);
        return ServerResponseEntity.success();
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.URI_PERMISSION_KEY, key = "#sysType")
    public void deleteMenuPermission(Long menuPermissionId, Integer sysType) {
        LambdaQueryWrapper<MenuPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MenuPermission::getMenuPermissionId, menuPermissionId)
                .eq(MenuPermission::getBizType, sysType);
        menuPermissionMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public List<String> permissions(Long userId, Integer sysType, boolean isAdmin) {
        //获取 proxy防止缓存注解失效
        MenuPermissionServiceImpl serviceImpl = (MenuPermissionServiceImpl) AopContext.currentProxy();
        List<String> permissions;
        if (isAdmin) {
            permissions = serviceImpl.getAllPermission(sysType);
        } else {
            permissions = serviceImpl.getPermissionByIdAndSysType(userId, sysType);
        }
        return permissions;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.URI_PERMISSION_KEY, key = "#sysType")
    public List<UriPermissionBO> getAllUriPermissions(Integer sysType) {
        return menuPermissionMapper.getAllUriPermissions(sysType);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.USER_PERMISSIONS_KEY, key = "#sysType + ':' + #userId"),
            @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId")
    })
    public void cleanUserPermissionsCache(Long userId, Integer sysType) {
    }

    @Cacheable(cacheNames = CacheNames.USER_PERMISSIONS_KEY, key = "#sysType + ':' + #userId")
    public List<String> getPermissionByIdAndSysType(Long userId, Integer sysType) {
        return menuPermissionMapper.selectAllFromMPAndMR(userId, sysType);
    }

    @Cacheable(cacheNames = CacheNames.PERMISSIONS_KEY, key = "#sysType")
    public List<String> getAllPermission(Integer sysType) {
        return menuPermissionMapper.selectPermissionFromMP(sysType);
    }
}




