package com.spring.what.multishop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 店铺详情
 * @TableName shop_detail
 */
@TableName(value ="shop_detail")
@Data
public class ShopDetail {
    /**
     * 店铺id
     */
    @TableId(type = IdType.AUTO)
    private Long shopId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺简介
     */
    private String intro;

    /**
     * 店铺logo(可修改)
     */
    private String shopLogo;

    /**
     * 店铺移动端背景图
     */
    private String mobileBackgroundPic;

    /**
     * 店铺状态(-1:已删除 0: 停业中 1:营业中)
     */
    private Integer shopStatus;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 身份证正面
     */
    private String identityCardFront;

    /**
     * 身份证反面
     */
    private String identityCardLater;

    /**
     * 店铺类型1自营店 2普通店
     */
    private Integer type;
}