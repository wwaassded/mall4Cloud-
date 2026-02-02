package com.spring.what.auth.mapper;

import com.spring.what.auth.model.AuthAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.security.bo.AuthAccountInVerifyBO;
import com.spring.what.security.constant.InputUsernameEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author whatyi
 * @description 针对表【auth_account(统一账户信息)】的数据库操作Mapper
 * @createDate 2026-01-31 11:19:54
 * @Entity com.spring.what.auth.model.AuthAccount
 */
@Mapper
public interface AuthAccountMapper extends BaseMapper<AuthAccount> {

    AuthAccountInVerifyBO getVerifiedUserInfoByInputUserName(@Param("userName") String principal, @Param("inputEnum") Integer inputUsernameEnum, @Param("sysType") Integer sysType);
}




