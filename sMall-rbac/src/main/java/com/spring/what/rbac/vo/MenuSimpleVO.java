package com.spring.what.rbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理VO
 *
 * @author FrozenWatermelon
 * @date 2020-09-15 16:35:01
 */
@Data
public class MenuSimpleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单id")
    private Long menuId;

    @Schema(description = "父菜单ID，一级菜单为0")
    private Long parentId;

    @Schema(description = "设置该路由在侧边栏和面包屑中展示的名字")
    private String title;

    @Schema(description = "菜单权限列表")
    private List<MenuPermissionSimpleVO> menuPermissions;

    @Override
    public String toString() {
        return "MenuSimpleVO{" +
                "menuId=" + menuId +
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", menuPermissions=" + menuPermissions +
                '}';
    }
}
