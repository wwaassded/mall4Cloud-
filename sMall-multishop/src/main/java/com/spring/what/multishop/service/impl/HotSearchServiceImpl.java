package com.spring.what.multishop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.multishop.dto.HotSearchDTO;
import com.spring.what.multishop.model.HotSearch;
import com.spring.what.multishop.service.HotSearchService;
import com.spring.what.multishop.mapper.HotSearchMapper;
import com.spring.what.multishop.vo.HotSearchVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【hot_search(热搜)】的数据库操作Service实现
 * @createDate 2026-02-26 13:46:07
 */
@Service
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch>
        implements HotSearchService {

    @Resource
    private HotSearchMapper hotSearchMapper;

    @Override
    public IPage<HotSearchVO> getPage(Page<HotSearchVO> page, HotSearchDTO hotSearchDTO) {
        return hotSearchMapper.getPage(page, hotSearchDTO);
    }

    @Override
    public HotSearchVO getByHotSearchId(Long hotSearchId) {
        LambdaQueryWrapper<HotSearch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotSearch::getHotSearchId, hotSearchId);
        HotSearch hotSearch = hotSearchMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.map(hotSearch, HotSearchVO.class);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.HOT_SEARCH_LIST_KEY, key = "#shopId")
    public void removeHotSearchListCache(Long shopId) {
    }

    @Override
    @Cacheable(cacheNames = CacheNames.HOT_SEARCH_LIST_KEY, key = "#shopId")
    public List<HotSearchVO> listByShopId(Long shopId) {
        return hotSearchMapper.list(shopId);
    }
}




