package com.spring.what.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.user.dto.UserRegisterDTO;
import com.spring.what.user.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.user.vo.UserSimpleInfoVO;
import jakarta.validation.Valid;

/**
 * @author whatyi
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2026-02-12 17:30:34
 */
public interface UserService extends IService<User> {

    IPage<UserApiVO> page(Page<UserApiVO> page);

    UserSimpleInfoVO getByAddrId();

    UserApiVO getUserApiVOById(Long userId);

    void updateUser(User user);

    Long saveFromUSerREgisterDTO(@Valid UserRegisterDTO param);
}
