package com.spring.what.user.feign;

import com.spring.what.api.user.feign.UserAddrFeignClient;
import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.AuthContext;
import com.spring.what.user.service.UserAddrService;
import jakarta.annotation.Resource;

public class UserAddrFeignController implements UserAddrFeignClient {

    @Resource
    private UserAddrService userAddrService;

    @Override
    public ServerResponseEntity<UserAddrVO> getUserAddrByAddrId(Long addrId) {
        UserAddrVO userAddrVO = userAddrService.getUserAddrVO(addrId, AuthContext.get().getUserId());
        return ServerResponseEntity.success(userAddrVO);
    }
}
