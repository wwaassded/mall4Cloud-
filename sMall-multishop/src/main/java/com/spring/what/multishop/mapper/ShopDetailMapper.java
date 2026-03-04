package com.spring.what.multishop.mapper;

import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.multishop.model.ShopDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author whatyi
 * @description 针对表【shop_detail(店铺详情)】的数据库操作Mapper
 * @createDate 2026-02-26 13:46:07
 * @Entity com.spring.what.multishop.model.ShopDetail
 */
public interface ShopDetailMapper extends BaseMapper<ShopDetail> {

    Integer countShopNumber(@Param("shopname") String shopName,@Param("shopid") Integer shopId);

    ShopDetailVO getByShopId(@Param("shopid") Long shopId);
}




