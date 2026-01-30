package com.spring.what.cache.util;


import cn.hutool.core.util.StrUtil;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {

    @SuppressWarnings("unchecked")
    private static final RedisTemplate<String, Object> REDIS_TEMPLATE = SpringContextUtils.getBean("redisTemplate",
            RedisTemplate.class);

    public static final StringRedisTemplate STRING_REDIS_TEMPLATE = SpringContextUtils.getBean("stringRedisTemplate",
            StringRedisTemplate.class);

    public static Boolean expire(String key, long time) {
        if (key.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        try {
            if (time > 0) {
                REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Set expire error:{}", e.getMessage());
            return Boolean.FALSE;
        }
    }

    public static Long getExpire(String key) {
        if (key.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        return REDIS_TEMPLATE.getExpire(key);
    }

    public static Boolean hasKey(String key) {
        if (key.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        try {
            return REDIS_TEMPLATE.hasKey(key);
        } catch (Exception e) {
            log.error("has Key error {}", e.getMessage());
            return Boolean.FALSE;
        }
    }

    public static void del(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                if (key.contains(StrUtil.SPACE)) {
                    throw new Mall4cloudException(ResponseEnum.EXCEPTION);
                }
            }
            if (keys.length == 1) {
                REDIS_TEMPLATE.delete(keys[0]);
            } else {
                REDIS_TEMPLATE.delete(Arrays.asList(keys));
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T get(String key) {
        if (key.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        return (T) REDIS_TEMPLATE.opsForValue().get(key);
    }

    public static boolean set(String key, Object value, long time) {
        if (key.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        try {
            if (time > 0) {
                REDIS_TEMPLATE.opsForValue().set(key, value, time);
            } else {
                REDIS_TEMPLATE.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("set error {}", e.getMessage());
            return false;
        }
    }

    public static void deleteBatch(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            if (key.contains(StrUtil.SPACE)) {
                throw new Mall4cloudException(ResponseEnum.EXCEPTION);
            }
        }
        REDIS_TEMPLATE.delete(keys);
    }

    public static void deleteBatch(String cacheName, List<?> cacheKeys) {
        if (StrUtil.isNotBlank(cacheName) || cacheKeys.isEmpty()) {
            return;
        }
        List<String> stringCacheKeys = cacheKeys.stream().map(String::valueOf).toList();
        List<String> keys = new ArrayList<>();
        for (String stringCacheKey : stringCacheKeys) {
            String key = cacheName + CacheNames.UNION + stringCacheKey;
            keys.add(key);
        }
        REDIS_TEMPLATE.delete(keys);
    }

    public static boolean cad(String key, String value) {
        if (key.contains(StrUtil.SPACE) || value.contains(StrUtil.SPACE)) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        String script = "if redis.call('get',KEY[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Long result = STRING_REDIS_TEMPLATE.execute(new DefaultRedisScript<Long>(script), List.of(key), value);
        return !Objects.equals(result, 0L);
    }

}
