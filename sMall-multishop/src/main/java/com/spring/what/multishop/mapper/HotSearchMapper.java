package com.spring.what.multishop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.multishop.dto.HotSearchDTO;
import com.spring.what.multishop.model.HotSearch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.multishop.vo.HotSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【hot_search(热搜)】的数据库操作Mapper
 * @createDate 2026-02-26 13:46:07
 * @Entity com.spring.what.multishop.model.HotSearch
 */
public interface HotSearchMapper extends BaseMapper<HotSearch> {

    IPage<HotSearchVO> getPage(Page<HotSearchVO> page, @Param("e") HotSearchDTO hotSearchDTO);

    List<HotSearchVO> list(@Param("shopId") Long shopId);
}




