package com.ruoyi.web.annotation;


import java.lang.annotation.*;

/**
 *  谷歌验证码校验确认
 * @author Administrator
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionVerifyCheck {
}
