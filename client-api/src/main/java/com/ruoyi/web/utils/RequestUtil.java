package com.ruoyi.web.utils;

import com.ruoyi.web.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;


@Slf4j
public class RequestUtil {

    /**
     * 从线程里面获取UID
     */
    public static String getWallet() {
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
            log.error("get user wallet address error!", ex);
            throw ex;
        }
    }

    public static String getLang() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (null == request.getHeader("Accept-Language")) {
                return "zh";
            }
            String lang = String.valueOf(request.getHeader("Accept-Language"));
            if (StringUtils.isBlank(lang)) {
                return "zh";
            }
            return Locale.forLanguageTag(lang).getLanguage();
        } catch (Exception ex) {
            return "zh";
        }
    }
}