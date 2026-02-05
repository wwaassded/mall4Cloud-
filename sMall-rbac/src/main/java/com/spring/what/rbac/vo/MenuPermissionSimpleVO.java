package com.spring.what.rbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单资源简易VO
 *
 * @author FrozenWatermelon
 * @date 2020-09-17 16:35:01
 */
@Data
public class MenuPermissionSimpleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单资源用户id")
    private Long menuPermissionId;

    @Schema(description = "资源关联菜单")
    private Long menuId;

    @Schema(description = "资源名称")
    private String name;

    @Override
    public String toString() {
        return "MenuPermissionVO{" +
                "menuPermissionId=" + menuPermissionId +
                ",menuId=" + menuId +
                ",name=" + name +
                '}';
    }
}
