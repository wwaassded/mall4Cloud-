package com.spring.what.platform.service.impl;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.IpHelper;
import com.spring.what.platform.dto.ChangeAccountDTO;
import com.spring.what.platform.mapper.SysUserMapper;
import com.spring.what.platform.model.SysUser;
import com.spring.what.platform.service.SysUserAccountService;
import com.spring.what.security.AuthContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserAccountServiceImpl implements SysUserAccountService {

    @Resource
    private AccountFeignClient accountFeignClient;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> save(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
        authAccountDTO.setSysType(SysTypeEnum.PLATFORM.value());
        authAccountDTO.setTenantId(null);
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        ServerResponseEntity<Long> save = accountFeignClient.save(authAccountDTO);
        if (!save.isSuccess()) {
            return ServerResponseEntity.transfer(save);
        }
        SysUser sysUser = new SysUser();
        sysUser.setSysUserId(authAccountDTO.getUserId());
        sysUser.setHasAccount(1);
        sysUserMapper.updateById(sysUser);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = getAuthAccountDTO(changeAccountDTO);
        ServerResponseEntity<Void> serverResponseEntity = accountFeignClient.update(authAccountDTO);
        if (!serverResponseEntity.isSuccess()) {
            return serverResponseEntity;
        }
        return ServerResponseEntity.success();
    }

    private AuthAccountDTO getAuthAccountDTO(ChangeAccountDTO changeAccountDTO) {
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        authAccountDTO.setSysType(userInfoInTokenBO.getSysType());
        authAccountDTO.setTenantId(userInfoInTokenBO.getTenantId());
        authAccountDTO.setUserId(changeAccountDTO.getUserId());
        authAccountDTO.setStatus(changeAccountDTO.getStatus());
        authAccountDTO.setPassword(changeAccountDTO.getPassword());
        authAccountDTO.setUsername(changeAccountDTO.getUsername());
        return authAccountDTO;
    }
}
