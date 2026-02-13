package com.spring.what.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.user.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.user.vo.UserSimpleInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author whatyi
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2026-02-12 17:30:33
 * @Entity com.spring.what.user.model.User
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<UserApiVO> getPage(Page<UserApiVO> page);

    UserSimpleInfoVO getByAddrId(@Param("userid") Long userId);

    UserApiVO selectById(@Param("userid") Long userId);
}




