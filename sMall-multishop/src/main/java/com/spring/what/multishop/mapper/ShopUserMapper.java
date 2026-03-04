package com.spring.what.multishop.mapper;

import com.spring.what.multishop.model.ShopUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.multishop.vo.ShopUserVO;
import org.apache.ibatis.annotations.Param;

/**
* @author whatyi
* @description 针对表【shop_user(商家用户)】的数据库操作Mapper
* @createDate 2026-02-26 13:46:07
* @Entity com.spring.what.multishop.model.ShopUser
*/
public interface ShopUserMapper extends BaseMapper<ShopUser> {

    ShopUserVO getByShopUserId(@Param("shopuserid") Long shopUserId);

    ShopUserVO getAdminUser(@Param("shopId") Long shopId);
}




