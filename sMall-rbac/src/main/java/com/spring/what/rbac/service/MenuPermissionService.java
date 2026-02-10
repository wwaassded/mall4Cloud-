package com.spring.what.rbac.service;

import com.spring.what.api.rbac.bo.UriPermissionBO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.rbac.model.MenuPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.rbac.vo.MenuPermissionVO;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【menu_permission(菜单资源)】的数据库操作Service
 * @createDate 2026-02-05 19:39:21
 */
public interface MenuPermissionService extends IService<MenuPermission> {

    List<MenuPermissionVO> listByMenuId(Long menuId);

    MenuPermissionVO getMenuPermissionId(Long menuPermissionId);

    ServerResponseEntity<Void> saveMenuPermission(MenuPermission menuPermission);

    ServerResponseEntity<Void> updateMenuPermission(MenuPermission menuPermission);

    void deleteMenuPermission(Long menuPermissionId, Integer sysType);

    List<String> permissions(Long userId, Integer sysType, boolean isAdmin);

    List<UriPermissionBO> getAllUriPermissions(Integer sysType);

    void cleanUserPermissionsCache(Long userId, Integer sysType);
}
