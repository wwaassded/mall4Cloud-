package com.spring.what.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 上传文件记录表
 * @TableName attach_file
 */
@TableName(value ="attach_file")
@Data
public class AttachFile {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long fileId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 文件 1:图片 2:视频 3:文件
     */
    private Integer type;

    /**
     * 文件分组id
     */
    private Long attachFileGroupId;
}