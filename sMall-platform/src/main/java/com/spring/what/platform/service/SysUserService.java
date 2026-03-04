package com.spring.what.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.platform.vo.SysUserSimpleVO;
import com.spring.what.platform.vo.SysUserVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
* @author whatyi
* @description 针对表【sys_user(平台用户)】的数据库操作Service
* @createDate 2026-02-21 18:56:26
*/
public interface SysUserService extends IService<SysUser> {

    SysUserSimpleVO getInfo(UserInfoInTokenBO userInfoInTokenBO);

    void saveDTO(SysUser sysUser, List<Long> roleIds);

    void updateDTO(SysUser sysUser, List<Long> roleIds);

    void deleteDTO(Long sysUserId);

    SysUserVO getVOById(@NotNull(message = "userId not null") Long userId);
}
