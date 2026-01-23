package com.spring.what.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseModel implements Serializable {

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;

    @Override
    public String toString() {
        return "BaseModel{" + "createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }

}
