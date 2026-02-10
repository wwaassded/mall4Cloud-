package com.spring.what.rbac.mapper;

import com.spring.what.api.rbac.bo.UriPermissionBO;
import com.spring.what.rbac.model.MenuPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.rbac.vo.MenuPermissionAndMenuRoleSimpleVO;
import com.spring.what.rbac.vo.MenuPermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【menu_permission(菜单资源)】的数据库操作Mapper
 * @createDate 2026-02-05 19:39:21
 * @Entity com.spring.what.rbac.model.MenuPermission
 */
public interface MenuPermissionMapper extends BaseMapper<MenuPermission> {

    List<MenuPermissionVO> listByMenuId(@Param("menuid") Long menuId);

    MenuPermissionVO getMenuPermissionId(@Param("menupermissionid") Long menuPermissionId);

    List<String> selectPermissionFromMP(@Param("systype") Integer sysType);

    List<String> selectAllFromMPAndMR(@Param("userid") Long userId,@Param("systype") Integer sysType);

    List<UriPermissionBO> getAllUriPermissions(@Param("systype") Integer sysType);
}




