package com.spring.what.common.database.config;

import cn.hutool.core.util.StrUtil;
import com.spring.what.common.constant.Auth;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RequestInterceptor.class, GlobalTransaction.class})
public class SeataInterceptorConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if (StrUtil.isNotBlank(xid) && !template.url().startsWith(Auth.CHECK_TOKEN_URI) && !template.url().startsWith(Auth.CHECK_RBAC_URI)) {
            template.header(RootContext.KEY_XID, xid);
        }
    }
}
