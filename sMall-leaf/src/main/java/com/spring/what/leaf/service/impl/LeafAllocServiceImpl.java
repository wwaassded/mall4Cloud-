package com.spring.what.leaf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.leaf.segment.model.LeafAlloc;
import com.spring.what.leaf.service.LeafAllocService;
import com.spring.what.leaf.mapper.LeafAllocMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【leaf_alloc】的数据库操作Service实现
 * @createDate 2026-01-23 11:50:37
 */
@Service
public class LeafAllocServiceImpl extends ServiceImpl<LeafAllocMapper, LeafAlloc>
        implements LeafAllocService {

    @Resource
    private LeafAllocMapper leafAllocMapper;

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        return this.list();
    }

    @Override
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        LambdaUpdateWrapper<LeafAlloc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LeafAlloc::getBizTag, tag);
        wrapper.setSql("max_id = max_id + step");
        this.update(wrapper);
        LambdaQueryWrapper<LeafAlloc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeafAlloc::getBizTag, tag);
        return leafAllocMapper.selectOne(queryWrapper);
    }

    @Override
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        LambdaUpdateWrapper<LeafAlloc> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LeafAlloc::getBizTag, leafAlloc.getBizTag());
        updateWrapper.setSql("max_id = max_id + " + leafAlloc.getStep());
        leafAllocMapper.update(updateWrapper);
        LambdaQueryWrapper<LeafAlloc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeafAlloc::getBizTag, leafAlloc.getBizTag());
        return leafAllocMapper.selectOne(queryWrapper);
    }

    @Override
    public List<String> getAllTags() {
        return leafAllocMapper.getAllTags();
    }
}




