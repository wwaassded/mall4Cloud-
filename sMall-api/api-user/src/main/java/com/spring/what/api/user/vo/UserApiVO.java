package com.spring.what.api.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.what.common.serializer.ImgJsonSerializer;
import com.spring.what.common.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户表VO
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserApiVO extends BaseVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "头像图片路径")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @Schema(description = "状态 1 正常 0 无效")
    private Integer status;

    @Schema(description = "是否创建过店铺")
    /**
     * openId list
     */
    private List<String> bizUserIdList;

    @Override
    public String toString() {
        return "UserApiVO{" +
                "userId=" + userId +
                ",createTime=" + createTime +
                ",updateTime=" + updateTime +
                ",nickName=" + nickName +
                ",pic=" + pic +
                ",status=" + status +
                '}';
    }
}
