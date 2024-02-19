package com.ruoyi.admin.filter;

import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.utils.LoginUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 过滤器
 */
@Component
@Slf4j
public class MyRequestContextFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        // swagger
        if (StringUtils.containsAny(url, "doc.html", "api-docs", "webjars","favicon.ico")) {
            filterChain.doFilter(request,response);
            return;
        }
        if (url.contains("public")) {
            filterChain.doFilter(request,response);
            return;
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            resolver.resolveException(request, response, null, new ServiceException("未登录", 500));
            return;
        }
        if (!LoginUtil.loginMap.containsKey(token)) {
            resolver.resolveException(request, response, null, new ServiceException("未登录", 500));
            return;
        }
        request.setAttribute("adminId", LoginUtil.loginMap.get(token));
        filterChain.doFilter(request,response);
    }

}