package com.spring.what.user.mapper;

import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.user.model.UserAddr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author whatyi
* @description 针对表【user_addr(用户地址)】的数据库操作Mapper
* @createDate 2026-02-12 17:30:34
* @Entity com.spring.what.user.model.UserAddr
*/
public interface UserAddrMapper extends BaseMapper<UserAddr> {

    List<UserAddrVO> listUserAddrVO(@Param("userid") Long userId);

    UserAddrVO getUserAddrVO(@Param("addrid") Long addrId,@Param("userid") Long userId);

    UserAddrVO getDefaultAddrVO(@Param("userid") Long userId);
}




