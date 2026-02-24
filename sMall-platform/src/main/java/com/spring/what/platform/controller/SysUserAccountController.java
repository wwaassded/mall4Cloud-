package com.spring.what.platform.controller;

import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.dto.ChangeAccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RequestMapping(value = "/sys_user/account")
@RestController
@Tag(name = "平台用户账号信息")
public class SysUserAccountController {

    @GetMapping
    @Operation(summary = "获取账号信息", description = "获取账号信息")
    public ServerResponseEntity<AuthAccountVO> getAccount(Long userId) {
        return null;
    }


    @PostMapping
    @Operation(summary = "添加账号", description = "添加账号")
    public ServerResponseEntity<Void> addAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        return null;
    }

    @PutMapping
    @Operation(summary = "修改账号", description = "修改账号")
    public ServerResponseEntity<Void> updateAccount(@Valid @RequestBody ChangeAccountDTO changeAccountDTO) {
        return null;
    }
}