package com.spring.what.multishop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.multishop.dto.HotSearchDTO;
import com.spring.what.multishop.model.HotSearch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.multishop.vo.HotSearchVO;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【hot_search(热搜)】的数据库操作Service
 * @createDate 2026-02-26 13:46:07
 */
public interface HotSearchService extends IService<HotSearch> {

    IPage<HotSearchVO> getPage(Page<HotSearchVO> page, HotSearchDTO hotSearchDTO);

    HotSearchVO getByHotSearchId(Long hotSearchId);

    void removeHotSearchListCache(Long hotSearchId);

    List<HotSearchVO> listByShopId(Long shopId);
}
