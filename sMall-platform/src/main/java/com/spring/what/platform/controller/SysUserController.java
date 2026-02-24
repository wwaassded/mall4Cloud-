package com.spring.what.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.rbac.feign.UserRoleFeignClient;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.platform.dto.SysUserDTO;
import com.spring.what.platform.model.SysUser;
import com.spring.what.platform.service.SysUserService;
import com.spring.what.platform.vo.SysUserSimpleVO;
import com.spring.what.platform.vo.SysUserVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/sys_user")
@RestController
@Tag(name = "平台用户信息")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private AccountFeignClient accountFeignClient;

    @GetMapping("/info")
    @Operation(summary = "登陆平台用户信息", description = "获取当前登陆平台用户的用户信息")
    public ServerResponseEntity<SysUserSimpleVO> info() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        return ServerResponseEntity.success(sysUserService.getInfo(userInfoInTokenBO));
    }

    @GetMapping("/page/{limit}/{size}")
    @Operation(summary = "平台用户列表", description = "获取平台用户列表")
    public ServerResponseEntity<IPage<SysUserVO>> page(@PathVariable Integer limit, @PathVariable Integer size, String nickName) {
        return ServerResponseEntity.showFail("not implement");
    }

    @GetMapping
    @Operation(summary = "获取平台用户信息", description = "根据用户id获取平台用户信息")
    public ServerResponseEntity<SysUserVO> detail(@RequestParam Long sysUserId) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getSysUserId, sysUserId);
        SysUser sysUser = sysUserService.getOne(lambdaQueryWrapper);
        SysUserVO sysUserVO = BeanUtil.map(sysUser, SysUserVO.class);
        ServerResponseEntity<List<Long>> roleIds = userRoleFeignClient.getRoleIds(sysUserId);
        sysUserVO.setRoleIds(roleIds.getData());
        return ServerResponseEntity.success(sysUserVO);
    }

    @PostMapping
    @Operation(summary = "保存平台用户信息", description = "保存平台用户信息")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysUserDTO sysUserDTO) {
        List<Long> roleIds = sysUserDTO.getRoleIds();
        SysUser sysUser = BeanUtil.map(sysUserDTO, SysUser.class);
        sysUserService.saveDTO(sysUser, roleIds);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新平台用户信息", description = "更新平台用户信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SysUserDTO sysUserDTO) {
        SysUser sysUser = BeanUtil.map(sysUserDTO, SysUser.class);
        sysUserService.updateDTO(sysUser, sysUserDTO.getRoleIds());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除平台用户信息", description = "根据平台用户id删除平台用户信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long sysUserId) {
        sysUserService.deleteDTO(sysUserId);
        return ServerResponseEntity.success();
    }
}