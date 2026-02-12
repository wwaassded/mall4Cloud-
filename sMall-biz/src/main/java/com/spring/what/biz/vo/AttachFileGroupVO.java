package com.spring.what.biz.vo;

import com.spring.what.common.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO
 *
 * @author YXF
 * @date 2020-12-04 16:15:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttachFileGroupVO extends BaseVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long attachFileGroupId;

    @Schema(description = "店铺id")
    private Long shopId;

    @Schema(description = "分组名称")
    private String name;

    @Override
    public String toString() {
        return "AttachFileGroupVO{" +
                "attachFileGroupId=" + attachFileGroupId +
                ",createTime=" + createTime +
                ",updateTime=" + updateTime +
                ",shopId=" + shopId +
                ",name=" + name +
                '}';
    }
}
