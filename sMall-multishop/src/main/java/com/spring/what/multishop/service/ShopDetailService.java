package com.spring.what.multishop.service;

import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.multishop.dto.ShopDetailDTO;
import com.spring.what.multishop.dto.UpdateShopPasswordDTO;
import com.spring.what.multishop.model.ShopDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.multishop.vo.ShopHeadInfoVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【shop_detail(店铺详情)】的数据库操作Service
 * @createDate 2026-02-26 13:46:07
 */
public interface ShopDetailService extends IService<ShopDetail> {

    void createShop(@Valid ShopDetailDTO shopDetailDTO);

    ShopDetailVO getMyShopDetailByShopId(Long shopId);

    Boolean checkShopName(String shopName);

    ShopHeadInfoVO getShopHeadInfo(Long shopId);

    void updateShopPassword(@Valid UpdateShopPasswordDTO updateShopPasswordDTO);

    List<ShopDetail> listByShopIds(List<Long> shopIds);

    List<ShopDetail> listByShopIdsAndShopName(List<Long> shopIds, String shopName);
}
