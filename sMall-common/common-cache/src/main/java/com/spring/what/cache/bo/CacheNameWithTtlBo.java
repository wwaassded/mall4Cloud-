package com.spring.what.cache.bo;

import lombok.Data;

@Data
public class CacheNameWithTtlBo {

    private String cacheName;

    private Integer ttl;

    public CacheNameWithTtlBo(String cacheName, Integer ttl) {
        this.cacheName = cacheName;
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "CacheNameWithTtlBO{" + "cacheName='" + cacheName + '\'' + ", ttl=" + ttl + '}';
    }

}
