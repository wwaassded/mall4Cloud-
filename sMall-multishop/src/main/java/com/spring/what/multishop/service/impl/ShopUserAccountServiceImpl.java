package com.spring.what.multishop.service.impl;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.IpHelper;
import com.spring.what.multishop.dto.ChangeAccountDTO;
import com.spring.what.multishop.mapper.ShopUserMapper;
import com.spring.what.multishop.model.ShopUser;
import com.spring.what.multishop.service.ShopUserAccountService;
import com.spring.what.security.AuthContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShopUserAccountServiceImpl implements ShopUserAccountService {

    @Resource
    private AccountFeignClient accountFeignClient;

    @Resource
    private ShopUserMapper shopUserMapper;

    @Override
    public AuthAccountVO getAccount(Long shopUserId, Integer sysType) {
        return accountFeignClient.getByUserIdAndSysType(shopUserId, sysType).getData();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> addAccount(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setIsAdmin(0);
        ServerResponseEntity<Long> serverResponseEntity = accountFeignClient.save(authAccountDTO);
        if (!serverResponseEntity.isSuccess()) {
            return ServerResponseEntity.transfer(serverResponseEntity);
        }
        ShopUser shopUser = new ShopUser();
        shopUser.setHasAccount(1);
        shopUser.setShopUserId(authAccountDTO.getUserId());
        shopUser.setShopId(AuthContext.get().getTenantId());
        shopUserMapper.updateById(shopUser);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
        ServerResponseEntity<Void> serverResponseEntity = accountFeignClient.update(authAccountDTO);
        if (!serverResponseEntity.isSuccess()) {
            return ServerResponseEntity.transfer(serverResponseEntity);
        }
        return ServerResponseEntity.success();
    }

    private AuthAccountDTO getAuthAccountDTO(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        authAccountDTO.setPassword(changeAccountDTO.getPassword());
        authAccountDTO.setUsername(changeAccountDTO.getUsername());
        authAccountDTO.setStatus(changeAccountDTO.getStatus());
        authAccountDTO.setSysType(userInfoInTokenBO.getSysType());
        authAccountDTO.setTenantId(userInfoInTokenBO.getTenantId());
        authAccountDTO.setUserId(changeAccountDTO.getUserId());
        return authAccountDTO;
    }
}
