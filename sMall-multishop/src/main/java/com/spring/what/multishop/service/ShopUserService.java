package com.spring.what.multishop.service;

import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.multishop.dto.ShopUserDTO;
import com.spring.what.multishop.model.ShopUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.multishop.vo.ShopUserVO;
import jakarta.validation.Valid;

/**
 * @author whatyi
 * @description 针对表【shop_user(商家用户)】的数据库操作Service
 * @createDate 2026-02-26 13:46:07
 */
public interface ShopUserService extends IService<ShopUser> {

    ShopUserVO getByShopUserId(Long shopUserId);

    void saveShopUserDTO(@Valid ShopUserDTO shopUserDTO);

    void updateShopUserDTO(@Valid ShopUserDTO shopUserDTO);

    void deleteByUserId(Long shopUserId);

}
