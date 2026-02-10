package com.spring.what.rbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MenuPermissionAndMenuRoleSimpleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单资源用户id")
    private Long menuPermissionId;

    @Schema(description = "相关下的所有的权限")
    private List<String> permissions;
}
