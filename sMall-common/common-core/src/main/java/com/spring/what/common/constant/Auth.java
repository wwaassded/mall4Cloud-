package com.spring.what.common.constant;

import com.spring.what.common.feign.FeignInsideAuthConfig;

public interface Auth {

    String CHECK_TOKEN_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/token/checkToken";

    String CHECK_RBAC_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/permission/checkPermission";
}