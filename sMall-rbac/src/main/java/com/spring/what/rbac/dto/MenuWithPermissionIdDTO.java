package com.spring.what.rbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单id和权限id列表
 *
 * @author FrozenWatermelon
 * @date 2020/9/18
 */
@Data
public class MenuWithPermissionIdDTO {

    @Schema(description = "菜单id")
    private Long menuId;

    @Schema(description = "菜单下的权限id列表")
    private List<Long> permissionIds;

    @Override
    public String toString() {
        return "MenuWithPermissionIdDTO{" +
                "menuId=" + menuId +
                ", permissionIds=" + permissionIds +
                '}';
    }
}
