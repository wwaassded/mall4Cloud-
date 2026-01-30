package com.spring.what.cache.adpter;

import com.spring.what.cache.bo.CacheNameWithTtlBo;

import java.util.List;

public interface CacheNameAdpter {

    List<CacheNameWithTtlBo> listCacheNameWithTtl();

}
