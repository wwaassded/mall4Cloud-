package com.spring.what.rbac.feign;

import cn.hutool.core.text.AntPathMatcher;
import com.spring.what.api.rbac.bo.UriPermissionBO;
import com.spring.what.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.spring.what.api.rbac.feign.PermissionFeignClient;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BooleanUtil;
import com.spring.what.rbac.service.MenuPermissionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
public class PermissionFeignController implements PermissionFeignClient {

    @Resource
    private MenuPermissionService menuPermissionService;

    @Override
    public ServerResponseEntity<Boolean> checkPermission(Long userId, Integer sysType, String uri, Integer isAdmin, Integer method) {
        Set<String> findingSet = new HashSet<>(menuPermissionService.permissions(userId, sysType, BooleanUtil.isTrue(isAdmin)));
        List<UriPermissionBO> allPermissions = menuPermissionService.getAllUriPermissions(sysType);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (UriPermissionBO permissionBO : allPermissions) {
            if (antPathMatcher.match(permissionBO.getPermission(), uri) && Objects.equals(method, permissionBO.getMethod())) {
                if (findingSet.contains(permissionBO.getPermission())) {
                    return ServerResponseEntity.success(Boolean.TRUE);
                } else {
                    return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
                }
            }
        }
        return ServerResponseEntity.success(Boolean.TRUE);
    }

    @Override
    public ServerResponseEntity<Void> clearUserPermissionsCache(ClearUserPermissionsCacheDTO clearUserPermissionsCacheDTO) {
        menuPermissionService.cleanUserPermissionsCache(clearUserPermissionsCacheDTO.getUserId(), clearUserPermissionsCacheDTO.getSysType());
        return ServerResponseEntity.success();
    }
}
