package com.spring.what.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.rbac.dto.UserRoleDTO;
import com.spring.what.api.rbac.feign.UserRoleFeignClient;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.model.SysUser;
import com.spring.what.platform.service.SysUserService;
import com.spring.what.platform.mapper.SysUserMapper;
import com.spring.what.platform.vo.SysUserSimpleVO;
import com.spring.what.platform.vo.SysUserVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author whatyi
 * &#064;description  针对表【sys_user(平台用户)】的数据库操作Service实现
 * &#064;createDate  2026-02-21 18:56:26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Override
    @Cacheable(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#userInfoInTokenBO.userId")
    public SysUserSimpleVO getInfo(UserInfoInTokenBO userInfoInTokenBO) {
        Long userId = userInfoInTokenBO.getUserId();
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getSysUserId, userId);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        SysUserSimpleVO sysUserSimpleVO = new SysUserSimpleVO();
        sysUserSimpleVO.setAvatar(sysUser.getAvatar());
        sysUserSimpleVO.setNickName(sysUser.getNickName());
        sysUserSimpleVO.setIsAdmin(userInfoInTokenBO.getIsAdmin());
        return sysUserSimpleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void saveDTO(SysUser sysUser, List<Long> roleIds) {
        sysUser.setSysUserId(null);
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setRoleIds(roleIds);
        sysUserMapper.insert(sysUser);
        userRoleDTO.setUserId(sysUser.getSysUserId());
        userRoleFeignClient.saveByUserIdAndSysType(userRoleDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#sysUser.sysUserId")
    public void updateDTO(SysUser sysUser, List<Long> roleIds) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(sysUser.getSysUserId());
        userRoleDTO.setRoleIds(roleIds);
        sysUserMapper.updateById(sysUser);
        userRoleFeignClient.updateByUserIdAndSysType(userRoleDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNames.PLATFORM_SIMPLE_INFO_KEY, key = "#sysUserId")
    public void deleteDTO(Long sysUserId) {
        sysUserMapper.deleteById(sysUserId);
        userRoleFeignClient.deleteByUserIdAndSysType(sysUserId);
        accountFeignClient.deleteByUserIdAndSysType(sysUserId);
    }

    @Override
    public SysUserVO getVOById(Long userId) {
        SysUserVO sysUserVO = sysUserMapper.getVOById(userId);
        ServerResponseEntity<List<Long>> roleIds = userRoleFeignClient.getRoleIds(userId);
        sysUserVO.setRoleIds(roleIds.getData());
        return sysUserVO;
    }
}




