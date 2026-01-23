package com.spring.what.leaf.mapper;

import com.spring.what.leaf.segment.model.LeafAlloc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author whatyi
* @description 针对表【leaf_alloc】的数据库操作Mapper
* @createDate 2026-01-23 11:50:37
* @Entity com.spring.what.leaf.segment.model.LeafAlloc
*/
public interface LeafAllocMapper extends BaseMapper<LeafAlloc> {

    List<String> getAllTags();
}




