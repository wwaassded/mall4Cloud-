package com.spring.what.security.filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//TODO 后序完成其中的token验证业务流程
@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

}
