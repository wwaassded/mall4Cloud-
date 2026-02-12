package com.spring.what.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName attach_file_group
 */
@TableName(value ="attach_file_group")
@Data
public class AttachFileGroup {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long attachFileGroupId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 1:图片 2:视频 3:文件
     */
    private Integer type;
}