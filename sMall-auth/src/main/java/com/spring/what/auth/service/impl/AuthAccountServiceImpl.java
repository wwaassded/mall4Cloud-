package com.spring.what.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.auth.model.AuthAccount;
import com.spring.what.auth.service.AuthAccountService;
import com.spring.what.auth.mapper.AuthAccountMapper;
import org.springframework.stereotype.Service;

/**
* @author whatyi
* @description 针对表【auth_account(统一账户信息)】的数据库操作Service实现
* @createDate 2026-01-31 11:19:54
*/
@Service
public class AuthAccountServiceImpl extends ServiceImpl<AuthAccountMapper, AuthAccount>
    implements AuthAccountService{

}




