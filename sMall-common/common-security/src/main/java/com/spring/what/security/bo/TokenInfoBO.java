package com.spring.what.security.bo;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import lombok.Data;

@Data
public class TokenInfoBO {

    /**
     * 保存在token信息里面的用户信息
     */
    private UserInfoInTokenBO userInfoInToken;

    private String accessToken;

    private String refreshToken;

    /**
     * 在多少秒后过期
     */
    private Integer expiresIn;

    @Override
    public String toString() {
        return "TokenInfoBO{" + "userInfoInToken=" + userInfoInToken + ", accessToken='" + accessToken + '\''
                + ", refreshToken='" + refreshToken + '\'' + ", expiresIn=" + expiresIn + '}';
    }

}