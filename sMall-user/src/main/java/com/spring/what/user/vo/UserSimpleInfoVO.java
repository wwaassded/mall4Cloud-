package com.spring.what.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author FrozenWatermelon
 * @date 2021/2/25
 */

@Data
public class UserSimpleInfoVO {

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickName;

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pic;

    @Override
    public String toString() {
        return "UserCenterInfoVO{" +
                "nickName='" + nickName + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
