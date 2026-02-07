package com.spring.what.rbac.mapper;

import com.spring.what.rbac.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.rbac.vo.MenuSimpleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【menu(菜单管理)】的数据库操作Mapper
 * @createDate 2026-02-05 19:39:21
 * @Entity com.spring.what.rbac.model.Menu
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuSimpleVO> listWithPermissions(@Param("systype") Integer sysType);

    List<Long> listMenuIds(@Param("userid") Long userId);
}




