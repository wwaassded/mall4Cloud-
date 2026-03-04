package com.spring.what.multishop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.api.rbac.dto.UserRoleDTO;
import com.spring.what.api.rbac.feign.UserRoleFeignClient;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.multishop.dto.ShopUserDTO;
import com.spring.what.multishop.model.ShopUser;
import com.spring.what.multishop.service.ShopUserService;
import com.spring.what.multishop.mapper.ShopUserMapper;
import com.spring.what.multishop.vo.ShopUserVO;
import com.spring.what.security.AuthContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【shop_user(商家用户)】的数据库操作Service实现
 * @createDate 2026-02-26 13:46:07
 */
@Service
public class ShopUserServiceImpl extends ServiceImpl<ShopUserMapper, ShopUser>
        implements ShopUserService {

    @Resource
    private ShopUserMapper shopUserMapper;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Override
    public ShopUserVO getByShopUserId(Long shopUserId) {
        ShopUserVO byShopUserId = shopUserMapper.getByShopUserId(shopUserId);
        ServerResponseEntity<List<Long>> roleIds = userRoleFeignClient.getRoleIds(byShopUserId.getShopUserId());
        if (!roleIds.isSuccess()) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        byShopUserId.setRoleIds(roleIds.getData());
        return byShopUserId;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void saveShopUserDTO(ShopUserDTO shopUserDTO) {
        List<Long> roleIds = shopUserDTO.getRoleIds();
        ShopUser shopUser = BeanUtil.map(shopUserDTO, ShopUser.class);
        shopUser.setShopId(AuthContext.get().getTenantId());
        shopUser.setShopUserId(null);
        shopUser.setHasAccount(0);
        shopUserMapper.insert(shopUser);
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(shopUser.getShopUserId());
        userRoleDTO.setRoleIds(roleIds);
        userRoleFeignClient.saveByUserIdAndSysType(userRoleDTO);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void updateShopUserDTO(ShopUserDTO shopUserDTO) {
        ShopUserVO dbShopUserVO = shopUserMapper.getByShopUserId(shopUserDTO.getShopUserId());
        if (dbShopUserVO == null || !Objects.equals(dbShopUserVO.getShopId(), AuthContext.get().getTenantId())) {
            throw new Mall4cloudException(ResponseEnum.UNAUTHORIZED);
        }
        List<Long> roleIds = shopUserDTO.getRoleIds();
        ShopUser shopUser = BeanUtil.map(shopUserDTO, ShopUser.class);
        shopUser.setShopId(dbShopUserVO.getShopId());
        shopUserMapper.updateById(shopUser);
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(shopUser.getShopUserId());
        userRoleDTO.setRoleIds(roleIds);
        userRoleFeignClient.updateByUserIdAndSysType(userRoleDTO);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void deleteByUserId(Long shopUserId) {
        shopUserMapper.deleteById(shopUserId);
        userRoleFeignClient.deleteByUserIdAndSysType(shopUserId);
        accountFeignClient.deleteByUserIdAndSysType(shopUserId);
    }
}




