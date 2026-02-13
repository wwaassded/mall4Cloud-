package com.spring.what.user.service;

import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.user.model.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【user_addr(用户地址)】的数据库操作Service
 * @createDate 2026-02-12 17:30:34
 */
public interface UserAddrService extends IService<UserAddr> {

    List<UserAddrVO> listUserAddrVO(Long userId);

    void removeDefaultAddr(Long userId);

    UserAddrVO getUserAddrVO(Long addrId, Long userId);
}
