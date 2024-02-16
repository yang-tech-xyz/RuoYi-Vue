package com.ruoyi.web.utils;

import com.ruoyi.web.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
public class RequestUtil {

    /**
     * 从线程里面获取UID
     */
    public static String getAdminId() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (null == request.getHeader("adminId")) {
                throw new ServiceException("未登录", 500);
            }
            String adminId = String.valueOf(request.getHeader("adminId"));
            if (StringUtils.isBlank(adminId)) {
                throw new ServiceException("未登录", 500);
            }
            return adminId;
        } catch (Exception ex) {
            log.error("get admin id error!", ex);
            throw ex;
        }
    }
}