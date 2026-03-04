package com.spring.what.multishop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 商家用户
 * @TableName shop_user
 */
@TableName(value ="shop_user")
@Data
public class ShopUser {
    /**
     * 商家用户id
     */
    @TableId
    private Long shopUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 关联店铺id
     */
    private Long shopId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 员工编号
     */
    private String code;

    /**
     * 联系方式
     */
    private String phoneNum;

    /**
     * 是否已经设置账号 0:未设置 1:已设置
     */
    private Integer hasAccount;
}