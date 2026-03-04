package com.spring.what.platform.controller;

import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.dto.ChangeAccountDTO;
import com.spring.what.platform.service.SysUserAccountService;
import com.spring.what.platform.service.SysUserService;
import com.spring.what.platform.vo.SysUserVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RequestMapping(value = "/sys_user/account")
@RestController
@Tag(name = "平台用户账号信息")
public class SysUserAccountController {

    @Resource
    private SysUserAccountService sysUserAccountService;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Resource
    private SysUserService sysUserService;

    @GetMapping
    @Operation(summary = "获取账号信息", description = "获取账号信息")
    public ServerResponseEntity<AuthAccountVO> getAccount(Long userId) {
        return accountFeignClient.getByUserIdAndSysType(userId, AuthContext.get().getSysType());
    }


    @PostMapping
    @Operation(summary = "添加账号", description = "添加账号")
    public ServerResponseEntity<Void> addAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        SysUserVO sysUserVO = sysUserService.getVOById(changeAccountDTO.getUserId());
        if (sysUserVO == null) {
            return ServerResponseEntity.showFail("无法获取账号信息");
        } else if (Objects.equals(sysUserVO.getHasAccount(), 1)) {
            return ServerResponseEntity.showFail("已经有账号了无须再次创建");
        }
        return sysUserAccountService.save(changeAccountDTO);
    }

    @PutMapping
    @Operation(summary = "修改账号", description = "修改账号")
    public ServerResponseEntity<Void> updateAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        SysUserVO sysUserVO = sysUserService.getVOById(changeAccountDTO.getUserId());
        if (sysUserVO == null || Objects.equals(sysUserVO.getHasAccount(), 0)) {
            return ServerResponseEntity.showFail("无法获取用户的账号信息");
        }
        return sysUserAccountService.update(changeAccountDTO);
    }
}