package com.spring.what.user.feign;

import com.spring.what.api.user.feign.UserFeignClient;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.user.model.User;
import com.spring.what.user.service.UserService;
import jakarta.annotation.Resource;

import java.util.List;

public class UserFeignController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Override
    public ServerResponseEntity<List<UserApiVO>> getUserByUserIds(List<Long> userIds) {
        List<User> users = userService.listByIds(userIds);
        List<UserApiVO> userApiVOS = BeanUtil.mapAsList(users, UserApiVO.class);
        return ServerResponseEntity.success(userApiVOS);
    }

    @Override
    public ServerResponseEntity<UserApiVO> getUserData(Long userId) {
        UserApiVO apiVOById = userService.getUserApiVOById(userId);
        return ServerResponseEntity.success(apiVOById);
    }
}
