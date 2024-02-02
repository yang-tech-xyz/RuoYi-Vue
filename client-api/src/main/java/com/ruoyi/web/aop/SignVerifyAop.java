package com.ruoyi.web.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.web.utils.DecodeMessageUtils;
import com.ruoyi.web.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 谷歌验证码验证AOP
 */
@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class SignVerifyAop {

	@Pointcut("@annotation(com.ruoyi.web.annotation.TransactionVerifyCheck)")
	private void transactionVerifyCheck() {
	}

	@Around("transactionVerifyCheck()")
	public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] objects = joinPoint.getArgs();

			JSONObject jsonObject = BeanUtil.copyProperties(objects[0], JSONObject.class);
			String parameter = WebUtils.getRequest().get().getParameter("transaction");
			String transaction = jsonObject.getString("transaction");
			if (StrUtil.isBlank(transaction)) {
				transaction = parameter;
			}
			 DecodeMessageUtils.validateSign(transaction);

		Object proceed = joinPoint.proceed();
		return proceed;
	}
}
