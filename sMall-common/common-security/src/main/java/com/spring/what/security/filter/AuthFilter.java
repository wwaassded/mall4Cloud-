package com.spring.what.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.feign.TokenFeignClient;
import com.spring.what.api.rbac.constant.HttpMethodEnum;
import com.spring.what.api.rbac.feign.PermissionFeignClient;
import com.spring.what.common.constant.Auth;
import com.spring.what.common.feign.FeignInsideAuthConfig;
import com.spring.what.common.handler.HttpHandler;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.IpHelper;
import com.spring.what.security.AuthContext;
import com.spring.what.security.adapter.AuthAdapter;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//TODO 后序完成其中的token验证业务流程
@Component
public class AuthFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Resource
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Resource
    private AuthAdapter authAdapter;

    @Resource
    private TokenFeignClient tokenFeignClient;

    @Resource
    private PermissionFeignClient permissionFeignClient;

    @Resource
    private HttpHandler httpHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String requestURI = servletRequest.getRequestURI();

        if (!insideFeignCheck(servletRequest)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }

        if (Auth.CHECK_TOKEN_URI.equals(requestURI)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        List<String> excludePathPaterns = authAdapter.excludePathPaterns();
        if (CollectionUtil.isNotEmpty(excludePathPaterns)) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (String pathPatern : excludePathPaterns) {
                if (antPathMatcher.match(pathPatern, requestURI)) {
                    chain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
        }
        String accessToken = servletRequest.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }
        ServerResponseEntity<UserInfoInTokenBO> userInfoInTokenBOServerResponseEntity = tokenFeignClient.checkToken(accessToken);
        if (!userInfoInTokenBOServerResponseEntity.isSuccess()) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }
        UserInfoInTokenBO userInfoInTokenBO = userInfoInTokenBOServerResponseEntity.getData();
        if (!rbacCheck(servletRequest, userInfoInTokenBO)) {
            httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
            return;
        }
        try {
            AuthContext.set(userInfoInTokenBO);
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            AuthContext.clean();
        }
    }

    private boolean rbacCheck(HttpServletRequest request, UserInfoInTokenBO userInfoInTokenBO) {
        if (!Objects.equals(userInfoInTokenBO.getSysType(), SysTypeEnum.MULTISHOP.value()) &&
                !Objects.equals(userInfoInTokenBO.getSysType(), SysTypeEnum.PLATFORM.value())) {
            return true;
        }
        ServerResponseEntity<Boolean> booleanServerResponseEntity = permissionFeignClient.checkPermission(userInfoInTokenBO.getUserId(),
                userInfoInTokenBO.getSysType(),
                request.getRequestURI(),
                userInfoInTokenBO.getIsAdmin(),
                HttpMethodEnum.valueOf(request.getMethod().toUpperCase()).value());
        if (!booleanServerResponseEntity.isSuccess()) {
            return false;
        }
        return booleanServerResponseEntity.getData();
    }

    private boolean insideFeignCheck(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!uri.startsWith(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX)) {
            return true;
        }
        String value = request.getHeader(feignInsideAuthConfig.getKey());
        if (StrUtil.isBlank(value) || !Objects.equals(value, feignInsideAuthConfig.getSecret())) {
            return false;
        }
        List<String> whiteIps = feignInsideAuthConfig.getIps();
        whiteIps.removeIf(StrUtil::isBlank);
        String ipAddr = IpHelper.getIpAddr();
        if (CollectionUtil.isNotEmpty(whiteIps) && whiteIps.contains(ipAddr)) {
            logger.error("ip not in ip White list: {}, ip, {}", whiteIps, IpHelper.getIpAddr());
            return false;
        }
        return true;
    }

}
