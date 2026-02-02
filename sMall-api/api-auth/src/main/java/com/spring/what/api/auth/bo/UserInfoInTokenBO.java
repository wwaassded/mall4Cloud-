package com.spring.what.api.auth.bo;

import com.spring.what.api.auth.constant.SysTypeEnum;

import lombok.Data;

@Data
public class UserInfoInTokenBO {

    /**
     * 用户在自己系统的用户id
     */
    private Long userId;

    /**
     * 全局唯一的id,
     */
    private Long uid;

    /**
     * 租户id (商家id)
     */
    private Long tenantId;

    /**
     * 系统类型
     *
     * @see SysTypeEnum
     */
    private Integer sysType;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    private String bizUserId;

    private String bizUid;

    @Override
    public String toString() {
        return "UserInfoInTokenBO{" +
                "userId=" + userId +
                ", uid=" + uid +
                ", tenantId=" + tenantId +
                ", sysType=" + sysType +
                ", isAdmin=" + isAdmin +
                ", bizUserId='" + bizUserId + '\'' +
                ", bizUid='" + bizUid + '\'' +
                '}';
    }
}