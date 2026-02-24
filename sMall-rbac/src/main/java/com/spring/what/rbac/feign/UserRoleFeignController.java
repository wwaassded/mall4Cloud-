package com.spring.what.rbac.feign;

import com.spring.what.api.rbac.dto.UserRoleDTO;
import com.spring.what.api.rbac.feign.UserRoleFeignClient;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.rbac.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRoleFeignController implements UserRoleFeignClient {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userRoleDTO.userId")
    public ServerResponseEntity<Void> saveByUserIdAndSysType(UserRoleDTO userRoleDTO) {
        if (userRoleDTO.getRoleIds().isEmpty()) {
            return ServerResponseEntity.success();
        }
        userRoleMapper.saveByUserIdAndSysType(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userRoleDTO.userId")
    public ServerResponseEntity<Void> updateByUserIdAndSysType(UserRoleDTO userRoleDTO) {
        userRoleMapper.deleteById(userRoleDTO.getUserId());
        if (userRoleDTO.getRoleIds().isEmpty()) {
            return ServerResponseEntity.success();
        }
        userRoleMapper.saveByUserIdAndSysType(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId")
    public ServerResponseEntity<Void> deleteByUserIdAndSysType(Long userId) {
        userRoleMapper.deleteById(userId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<Long>> getRoleIds(Long userId) {
        List<Long> roleIds = userRoleMapper.getRoleIds(userId);
        return ServerResponseEntity.success(roleIds);
    }
}
