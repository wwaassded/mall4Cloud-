package com.spring.what.api.user.vo;

import com.spring.what.common.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 省市区地区信息VO
 *
 * @author YXF
 * @date 2020-11-25 15:16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaVO extends BaseVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long areaId;

    @Schema(description = "地址")
    private String areaName;

    @Schema(description = "上级地址")
    private Long parentId;

    @Schema(description = "等级（从1开始）")
    private Integer level;

    private Integer check;

    /**
     * 下级地址集合
     */
    private List<AreaVO> areas;

    /**
     * 下级地址的areaId
     */
    private List<Long> areaIds;

    @Override
    public String toString() {
        return "AreaDTO{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", check=" + check +
                ", areas=" + areas +
                ", areaIds=" + areaIds +
                '}';
    }
}
