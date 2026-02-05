package com.spring.what.rbac.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 菜单资源
 * @TableName menu_permission
 */
@TableName(value ="menu_permission")
@Data
public class MenuPermission {
    /**
     * 菜单资源用户id
     */
    @TableId(type = IdType.AUTO)
    private Long menuPermissionId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 资源关联菜单
     */
    private Long menuId;

    /**
     * 业务类型 1 店铺菜单 2平台菜单
     */
    private Integer bizType;

    /**
     * 权限对应的编码
     */
    private String permission;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源对应服务器路径
     */
    private String uri;

    /**
     * 请求方法 1.GET 2.POST 3.PUT 4.DELETE
     */
    private Integer method;
}