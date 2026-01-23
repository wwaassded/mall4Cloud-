package com.spring.what.leaf.segment;

import com.spring.what.leaf.IDGen;
import com.spring.what.leaf.common.Result;
import com.spring.what.leaf.service.LeafAllocService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component
public class SegmentIDGenImpl implements IDGen {

    @Resource
    private LeafAllocService leafAllocService;

    @Override
    public Result getId(String key) {
        return null;
    }

    @Override
    public boolean init() {
        return false;
    }
}
