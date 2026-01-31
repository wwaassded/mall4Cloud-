package com.spring.what.security.adapter;

import com.spring.what.common.feign.FeignInsideAuthConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultAuthAdapter implements AuthAdapter {


    /**
     * 内部直接调用接口，无需登录权限
     */
    private static final String FEIGN_INSIDER_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/**";

    /**
     * 外部直接调用接口，无需登录权限 unwanted auth
     */
    private static final String EXTERNAL_URI = "/**/ua/**";

    /**
     * swagger
     */
    private static final String DOC_URI = "/v3/api-docs";

    @Override
    public List<String> pathPatterns() {
        return Collections.singletonList("/*");
    }

    @Override
    public List<String> excludePathPaterns() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(FEIGN_INSIDER_URI);
        arrayList.add(EXTERNAL_URI);
        arrayList.add(DOC_URI);
        return arrayList;
    }
}
