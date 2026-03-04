package com.spring.what.platform.service;

import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.platform.dto.ChangeAccountDTO;
import jakarta.validation.Valid;

public interface SysUserAccountService {
    ServerResponseEntity<Void> save(@Valid ChangeAccountDTO changeAccountDTO);

    ServerResponseEntity<Void> update(@Valid ChangeAccountDTO changeAccountDTO);
}
