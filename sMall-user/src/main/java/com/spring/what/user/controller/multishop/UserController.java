package com.spring.what.user.controller.multishop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.user.dto.UserDTO;
import com.spring.what.user.model.User;
import com.spring.what.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController("multishopUserController")
@RequestMapping("/m/user")
@Tag(name = "店铺-用户表")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page/{limit}/{size}")
    @Operation(summary = "获取用户表列表", description = "分页获取用户表列表")
    public ServerResponseEntity<IPage<UserApiVO>> page(@PathVariable Integer limit, @PathVariable Integer size) {
        Page<UserApiVO> page = new Page<>(limit, size);
        IPage<UserApiVO> res = userService.page(page);
        return ServerResponseEntity.success(res);
    }

    @GetMapping
    @Operation(summary = "获取用户表", description = "根据userId获取用户表")
    @Cacheable(cacheNames = CacheNames.USER_INFO, key = "#userId")
    public ServerResponseEntity<UserApiVO> getByUserId(@RequestParam Long userId) {
        User user = userService.getById(userId);
        return ServerResponseEntity.success(BeanUtil.map(user, UserApiVO.class));
    }


    @PutMapping
    @Operation(summary = "更新用户表", description = "更新用户表")
    @CacheEvict(cacheNames = CacheNames.USER_INFO, key = "#userDTO.userId")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserDTO userDTO) {
        User user = BeanUtil.map(userDTO, User.class);
        userService.updateById(user);
        return ServerResponseEntity.success();
    }

}
