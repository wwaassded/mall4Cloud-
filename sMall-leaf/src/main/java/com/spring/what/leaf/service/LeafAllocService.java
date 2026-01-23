package com.spring.what.leaf.service;

import com.spring.what.leaf.segment.model.LeafAlloc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【leaf_alloc】的数据库操作Service
 * @createDate 2026-01-23 11:50:37
 */
public interface LeafAllocService extends IService<LeafAlloc> {
    List<LeafAlloc> getAllLeafAllocs();

    /**
     * updateMaxIdAndGetLeafAlloc
     *
     * @param tag tag
     * @return LeafAlloc
     */
    LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

    /**
     * updateMaxIdByCustomStepAndGetLeafAlloc
     *
     * @param leafAlloc leafAlloc
     * @return LeafAlloc
     */
    LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

    /**
     * getAllTags
     *
     * @return List<String>
     */
    List<String> getAllTags();
}
