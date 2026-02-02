package com.spring.what.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新密码
 *
 * @author FrozenWatermelon
 * @date 2020/09/21
 */
@Data
public class UpdatePasswordDTO {

    @NotBlank(message = "oldPassword NotBlank")
    @Schema(description = "旧密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @NotNull(message = "newPassword NotNull")
    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;

    @Override
    public String toString() {
        return "UpdatePasswordDTO{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
