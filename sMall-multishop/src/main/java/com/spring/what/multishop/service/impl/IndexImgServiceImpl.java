package com.spring.what.multishop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.multishop.dto.IndexImgDTO;
import com.spring.what.multishop.model.IndexImg;
import com.spring.what.multishop.service.IndexImgService;
import com.spring.what.multishop.mapper.IndexImgMapper;
import com.spring.what.multishop.vo.IndexImgVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【index_img(轮播图)】的数据库操作Service实现
 * @createDate 2026-02-26 13:46:07
 */
@Service
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg>
        implements IndexImgService {

    @Resource
    private IndexImgMapper indexImgMapper;

    @Override
    public IPage<IndexImgVO> pageVO(Page<IndexImgVO> page, IndexImgDTO indexImgDTO) {
        return indexImgMapper.pageVO(page, indexImgDTO);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.INDEX_IMG_KEY, key = "#shopId")
    public void removeByIdAndShopId(Long imgId, Long shopId) {
        LambdaQueryWrapper<IndexImg> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(IndexImg::getImgId, imgId)
                .eq(IndexImg::getShopId, shopId);
        indexImgMapper.delete(lambdaQueryWrapper);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.INDEX_IMG_KEY, key = "#shopId")
    public List<IndexImgVO> getList(Long shopId) {
        return indexImgMapper.getList(shopId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.INDEX_IMG_KEY, key = "#spuId"),
            @CacheEvict(cacheNames = CacheNames.INDEX_IMG_KEY, key = "0")
    })
    public void deleteBySpuId(Long spuId, Long shopId) {
        LambdaQueryWrapper<IndexImg> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(IndexImg::getShopId, shopId)
                .eq(IndexImg::getSpuId, spuId);
        indexImgMapper.delete(lambdaQueryWrapper);
    }
}




