package com.spring.what.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新token
 *
 * @author FrozenWatermelon
 * @date 2020/7/1
 */
@Data
public class RefreshTokenDTO {

    /**
     * refreshToken
     */
    @NotBlank(message = "refreshToken不能为空")
    @Schema(description = "refreshToken", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @Override
    public String toString() {
        return "RefreshTokenDTO{" + "refreshToken='" + refreshToken + '\'' + '}';
    }

}
