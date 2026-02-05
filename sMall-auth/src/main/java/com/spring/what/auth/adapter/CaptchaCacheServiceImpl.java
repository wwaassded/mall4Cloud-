package com.spring.what.auth.adapter;

import com.anji.captcha.service.CaptchaCacheService;
import com.spring.what.cache.util.RedisUtil;

public class CaptchaCacheServiceImpl implements CaptchaCacheService {
    @Override
    public void set(String s, String s1, long l) {
        RedisUtil.set(s, s1, l);
    }

    @Override
    public boolean exists(String s) {
        return RedisUtil.hasKey(s);
    }

    @Override
    public void delete(String s) {
        RedisUtil.del(s);
    }

    @Override
    public String get(String s) {
        return RedisUtil.get(s);
    }

    @Override
    public String type() {
        return "redis";
    }
}
