package com.spring.what.user.controller.app;

import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.security.AuthContext;
import com.spring.what.user.model.User;
import com.spring.what.user.service.UserService;
import com.spring.what.user.vo.UserSimpleInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController("appUserController")
@RequestMapping("/a/user")
@Tag(name = "app-用户信息")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/simple_info")
    @Operation(summary = "用户头像昵称", description = "用户头像昵称")
    public ServerResponseEntity<UserSimpleInfoVO> getByAddrId() {
        UserApiVO userApiVO = userService.getUserApiVOById(AuthContext.get().getUserId());
        UserSimpleInfoVO simpleInfoVO = new UserSimpleInfoVO();
        simpleInfoVO.setPic(userApiVO.getPic());
        simpleInfoVO.setNickName(userApiVO.getNickName());
        return ServerResponseEntity.success(simpleInfoVO);
    }


    @GetMapping("/ma/user_detail_info")
    @Operation(summary = "获取用户详细信息", description = "返回用户详细信息")
    public ServerResponseEntity<UserApiVO> getUserDetailInfo() {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.fail(ResponseEnum.CLEAN_TOKEN);
        }
        Long userId = userInfoInTokenBO.getUserId();
        UserApiVO userApiVO = userService.getUserApiVOById(userId);
        return ServerResponseEntity.success(userApiVO);
    }

    @PostMapping("/ma/update_user")
    @Operation(summary = "更新用户信息")
    public ServerResponseEntity<Void> updateUser(@RequestBody UserApiVO userApiVO) {
        Long userId = AuthContext.get().getUserId();
        UserApiVO apiVOById = userService.getUserApiVOById(userId);
        User user = new User();
        user.setUserId(userId);
        user.setPic(userApiVO.getPic() == null ? apiVOById.getPic() : userApiVO.getPic());
        user.setNickName(userApiVO.getNickName() == null ? apiVOById.getNickName() : userApiVO.getNickName());
        userService.updateUser(user);
        return ServerResponseEntity.success();
    }
}

