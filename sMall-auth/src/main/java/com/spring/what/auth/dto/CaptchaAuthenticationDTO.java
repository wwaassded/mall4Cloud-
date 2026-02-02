package com.spring.what.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 验证码登陆
 *
 * @author FrozenWatermelon
 * @date 2020/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {

    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String captchaVerification;

    @Override
    public String toString() {
        return "CaptchaAuthenticationDTO{" + "captchaVerification='" + captchaVerification + '\'' + "} "
                + super.toString();
    }

}
