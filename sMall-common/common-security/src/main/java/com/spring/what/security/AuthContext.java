package com.spring.what.security;


import com.spring.what.api.auth.bo.UserInfoInTokenBO;

public class AuthContext {
    static private final ThreadLocal<UserInfoInTokenBO> USER_INFO_IN_TOKEN_HOLDER = new ThreadLocal<>();

    static public UserInfoInTokenBO get() {
        return USER_INFO_IN_TOKEN_HOLDER.get();
    }

    static public void set(UserInfoInTokenBO userInfoInTokenBO) {
        USER_INFO_IN_TOKEN_HOLDER.set(userInfoInTokenBO);
    }

    static public void clean() {
        if (USER_INFO_IN_TOKEN_HOLDER.get() != null) {
            USER_INFO_IN_TOKEN_HOLDER.remove();
        }
    }
}
