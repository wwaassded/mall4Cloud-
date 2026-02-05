package com.spring.what.auth.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import lombok.Data;

/**
 * 统一账户信息
 *
 * @TableName auth_account
 */
@TableName(value = "auth_account")
@Data
public class AuthAccount {
    /**
     * 全平台用户唯一id
     */
    @TableId
    private Long uid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建ip
     */
    private String createIp;

    /**
     * 状态 1:启用 0:禁用 -1:删除
     */
    @TableLogic(value = "1", delval = "-1")
    private Integer status;

    /**
     * 用户类型见SysTypeEnum 0.普通用户系统 1.商家端 2平台端
     */
    private Integer sysType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 所属租户
     */
    private Long tenantId;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;
}