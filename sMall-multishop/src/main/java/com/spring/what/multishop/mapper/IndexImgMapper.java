package com.spring.what.multishop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.multishop.dto.IndexImgDTO;
import com.spring.what.multishop.model.IndexImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.multishop.vo.IndexImgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【index_img(轮播图)】的数据库操作Mapper
 * @createDate 2026-02-26 13:46:07
 * @Entity com.spring.what.multishop.model.IndexImg
 */
public interface IndexImgMapper extends BaseMapper<IndexImg> {

    IPage<IndexImgVO> pageVO(Page<IndexImgVO> page, @Param("indexImg") IndexImgDTO indexImgDTO);

    List<IndexImgVO> getList(@Param("shopId") Long shopId);
}




