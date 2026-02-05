package com.spring.what.rbac.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 角色
 * @TableName role
 */
@TableName(value ="role")
@Data
public class Role {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 业务类型 1 店铺菜单 2平台菜单
     */
    private Integer bizType;

    /**
     * 所属租户
     */
    private Long tenantId;
}