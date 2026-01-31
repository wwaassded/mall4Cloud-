package com.spring.what.security.constant;

import lombok.Getter;

@Getter
public enum InputUsernameEnum {

    PHONE(1),

    USERNAME(2),

    EMAIL(3);

    public final Integer value;

    InputUsernameEnum(Integer value) {
        this.value = value;
    }
}
