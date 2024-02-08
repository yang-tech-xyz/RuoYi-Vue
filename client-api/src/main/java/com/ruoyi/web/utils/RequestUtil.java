package com.ruoyi.web.utils;

import com.ruoyi.web.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {

    /**
     * 从线程里面获取UID
     */
    public static String getWalletAddress() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (null == request.getHeader("walletAddress")) {
                throw new ServiceException("未登录", 500);
            }
            String walletAddress = String.valueOf(request.getHeader("walletAddress"));
            if (StringUtils.isBlank(walletAddress)) {
                throw new ServiceException("未登录", 500);
            }
            return walletAddress.toLowerCase();
        } catch (Exception ex) {
            throw ex;
        }
    }
}