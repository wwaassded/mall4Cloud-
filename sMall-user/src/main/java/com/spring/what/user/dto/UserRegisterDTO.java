package com.spring.what.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhd
 * @date 2020/12/30
 */
@Getter
@Setter
@Schema(description = "用户注册信息")
public class UserRegisterDTO {

    @NotBlank
    @Schema(description = "密码")
    private String password;

    @Schema(description = "头像")
    private String img;

    @Schema(description = "昵称")
    private String nickName;

    @NotBlank
    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "当账户未绑定时，临时的uid")
    private String tempUid;

    @Schema(description = "用户id")
    private Long userId;

    @Override
    public String toString() {
        return "UserRegisterDTO{" +
                "password='" + password + '\'' +
                ", img='" + img + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", tempUid='" + tempUid + '\'' +
                ", userId=" + userId +
                '}';
    }
}
