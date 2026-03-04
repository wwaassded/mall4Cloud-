package com.spring.what.multishop.service;

import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.dto.ChangeAccountDTO;
import jakarta.validation.Valid;

public interface ShopUserAccountService {
    AuthAccountVO getAccount(Long shopUserId, Integer sysType);

    ServerResponseEntity<Void> addAccount(@Valid ChangeAccountDTO changeAccountDTO);

    ServerResponseEntity<Void> update(@Valid ChangeAccountDTO changeAccountDTO);
}
