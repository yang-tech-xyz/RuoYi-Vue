package com.ruoyi.web.filter;

import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.utils.LoginUtil;
import com.ruoyi.web.utils.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 过滤器
 */
@Slf4j
public class RequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Filter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        initializeRequestAttributes((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }


    /**
     * 处理Request参数
     */
    private void initializeRequestAttributes(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (url.contains("public")) {
            return;
        }
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("未登录", 500);
        }
        if (!LoginUtil.loginMap.containsKey(token)) {
            throw new ServiceException("未登录", 500);
        }
        request.setAttribute("adminId", LoginUtil.loginMap.get(token));
    }

}
