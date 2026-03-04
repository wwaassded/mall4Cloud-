package com.spring.what.multishop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.multishop.dto.IndexImgDTO;
import com.spring.what.multishop.model.IndexImg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.multishop.vo.IndexImgVO;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【index_img(轮播图)】的数据库操作Service
 * @createDate 2026-02-26 13:46:07
 */
public interface IndexImgService extends IService<IndexImg> {

    IPage<IndexImgVO> pageVO(Page<IndexImgVO> page, IndexImgDTO indexImgDTO);

    void removeByIdAndShopId(Long imgId, Long shopId);

    List<IndexImgVO> getList(Long shopId);

    void deleteBySpuId(Long spuId, Long shopId);
}
