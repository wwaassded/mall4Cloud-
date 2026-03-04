package com.spring.what.multishop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 热搜
 * @TableName hot_search
 */
@TableName(value ="hot_search")
@Data
public class HotSearch {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long hotSearchId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 店铺ID 0为全平台热搜
     */
    private Long shopId;

    /**
     * 内容
     */
    private String content;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 状态 0下线 1上线
     */
    private Integer status;

    /**
     * 热搜标题
     */
    private String title;
}