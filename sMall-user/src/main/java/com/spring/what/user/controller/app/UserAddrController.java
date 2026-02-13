package com.spring.what.user.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.security.AuthContext;
import com.spring.what.user.dto.UserAddrDTO;
import com.spring.what.user.model.UserAddr;
import com.spring.what.user.service.UserAddrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appUserAddrController")
@RequestMapping("/user_addr")
@Tag(name = "app-用户地址")
public class UserAddrController {

    @Resource
    private UserAddrService userAddrService;

    private static final Integer MAX_USER_ADDR = 10;

    @GetMapping("/list")
    @Operation(summary = "获取用户地址列表", description = "获取用户地址列表")
    public ServerResponseEntity<List<UserAddrVO>> list() {
        Long userId = AuthContext.get().getUserId();
        return ServerResponseEntity.success(userAddrService.listUserAddrVO(userId));
    }

    @GetMapping
    @Operation(summary = "获取用户地址", description = "根据addrId获取用户地址")
    public ServerResponseEntity<UserAddrVO> getByAddrId(@RequestParam Long addrId) {
        Long userId = AuthContext.get().getUserId();
        LambdaQueryWrapper<UserAddr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddr::getAddrId, addrId)
                .eq(UserAddr::getUserId, userId);
        UserAddr userAddr = userAddrService.getOne(lambdaQueryWrapper);
        UserAddrVO userAddrVO = BeanUtil.map(userAddr, UserAddrVO.class);
        return ServerResponseEntity.success(userAddrVO);
    }

    @PostMapping
    @Operation(summary = "保存用户地址", description = "保存用户地址")
    public ServerResponseEntity<Void> save(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        Long userId = AuthContext.get().getUserId();
        LambdaQueryWrapper<UserAddr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddr::getUserId, userId);
        long addrNumber = userAddrService.count(lambdaQueryWrapper);
        if (addrNumber == MAX_USER_ADDR) {
            return ServerResponseEntity.showFail("地址数量已经达到最大无法新增地址");
        }
        UserAddr userAddr = BeanUtil.map(userAddrDTO, UserAddr.class);
        userAddr.setAddrId(null);
        userAddr.setUserId(AuthContext.get().getUserId());
        if (addrNumber == 0) {
            userAddr.setIsDefault(UserAddr.DEFAULT_ADDR);
        } else if (!UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddr.setIsDefault(UserAddr.NOT_DEFAULT_ADDR);
        }
        userAddrService.save(userAddr);
        if (UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddrService.removeDefaultAddr(userId);
        }
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新用户地址", description = "更新用户地址")
    public ServerResponseEntity<Void> update(@Valid @RequestBody UserAddrDTO userAddrDTO) {
        Long userId = AuthContext.get().getUserId();
        UserAddr userAddr = BeanUtil.map(userAddrDTO, UserAddr.class);
        LambdaQueryWrapper<UserAddr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddr::getUserId, userId)
                .eq(UserAddr::getAddrId, userAddr.getAddrId());
        UserAddr dbUserAddr = userAddrService.getOne(lambdaQueryWrapper);
        if (dbUserAddr == null) {
            throw new Mall4cloudException("用户地址已经被删除");
        }
        if (UserAddr.DEFAULT_ADDR.equals(dbUserAddr.getIsDefault()) && !UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            throw new Mall4cloudException(ResponseEnum.DATA_ERROR);
        }
        userAddr.setUserId(userId);
        userAddrService.updateById(userAddr);
        if (UserAddr.DEFAULT_ADDR.equals(userAddr.getIsDefault())) {
            userAddrService.removeDefaultAddr(userId);
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除用户地址", description = "根据用户地址id删除用户地址")
    public ServerResponseEntity<Void> delete(@RequestParam Long addrId) {
        Long userId = AuthContext.get().getUserId();
        LambdaQueryWrapper<UserAddr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddr::getAddrId, addrId)
                .eq(UserAddr::getUserId, userId);
        UserAddr dbUserAddr = userAddrService.getOne(lambdaQueryWrapper);
        if (dbUserAddr == null) {
            throw new Mall4cloudException("用户地址已经被删除");
        } else if (UserAddr.DEFAULT_ADDR.equals(dbUserAddr.getIsDefault())) {
            throw new Mall4cloudException("默认地址不能被删除");
        }
        userAddrService.removeById(lambdaQueryWrapper);
        return ServerResponseEntity.success();
    }

}