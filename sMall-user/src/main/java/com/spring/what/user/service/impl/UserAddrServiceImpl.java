package com.spring.what.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.user.model.UserAddr;
import com.spring.what.user.service.UserAddrService;
import com.spring.what.user.mapper.UserAddrMapper;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【user_addr(用户地址)】的数据库操作Service实现
 * @createDate 2026-02-12 17:30:34
 */
@Service
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr>
        implements UserAddrService {

    @Resource
    private UserAddrMapper userAddrMapper;

    @Override
    public List<UserAddrVO> listUserAddrVO(Long userId) {
        return userAddrMapper.listUserAddrVO(userId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.USER_DEFAULT_ADDR, key = "#userId")
    public void removeDefaultAddr(Long userId) {
    }

    @Override
    public UserAddrVO getUserAddrVO(Long addrId, Long userId) {
        if (addrId == null || addrId == 0L) {
            return userAddrMapper.getDefaultAddrVO(userId);
        }
        return userAddrMapper.getUserAddrVO(addrId, userId);
    }
}




