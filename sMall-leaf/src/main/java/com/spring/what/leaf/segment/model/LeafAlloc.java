package com.spring.what.leaf.segment.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName leaf_alloc
 */
@TableName(value ="leaf_alloc")
@Data
public class LeafAlloc {
    /**
     * 区分业务
     */
    @TableId
    private String bizTag;

    /**
     * 该biz_tag目前所被分配的ID号段的最大值
     */
    private Long maxId;

    /**
     * 每次分配的号段长度
     */
    private Integer step;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 每次getid时随机增加的长度，这样就不会有连续的id了
     */
    private Integer randomStep;
}