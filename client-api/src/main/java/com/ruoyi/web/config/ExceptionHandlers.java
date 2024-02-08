package com.ruoyi.web.config;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers {

    /**
     * 重新定义异常处理器内容：
     * 1.后端不在处理message信息，统一返回code码，由前端进行key+value获取内容。
     * 2.为保证前端能够正确获取到对应的信息，此处需要进行包装处理
     * 3.此方法统一使用error返回，针对bind异常，采用illegal_argument返回，并且此处需要使用format将框架message进行包装返回。如：xxx{0},xxx
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<AjaxResult> exception(HttpServletRequest request, Throwable throwable) {
        logParams(request, throwable);
        AjaxResult ajaxResult;
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) throwable;
            String msg = validException.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
            ajaxResult = AjaxResult.error(500, msg);
        } else if (throwable instanceof ServletRequestBindingException || throwable instanceof BindException) {
            String msg = throwable.getMessage();
            if (throwable instanceof BindException) {
                BindException bindException = (BindException) throwable;
                msg = bindException.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
            }
            ajaxResult = AjaxResult.error(500, msg);
        } else if (throwable instanceof ServletException) {
            ajaxResult = AjaxResult.error(500, throwable.getMessage());
        } else if (throwable instanceof ServiceException) {
            ServiceException exception = (ServiceException) throwable;
            ajaxResult = AjaxResult.error(exception.getCode(), exception.getMessage());
        } else {
            ajaxResult = AjaxResult.error(500, throwable.getMessage());
        }
        // 异常码转换,动态处理String.format
        return new ResponseEntity<>(ajaxResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 打印请求异常日志
     */
    private void logParams(HttpServletRequest request, Throwable throwable) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<String> paramStringList = parameterMap.keySet().stream()
                .map(e -> e + "=" + StringUtils.join(parameterMap.get(e), ","))
                .collect(Collectors.toList());
        String message = throwable.getMessage();
        log.warn("【异常处理器】 -> 异常捕获：\n{}:\nrequestURI: {}\nparams:{}", message, request.getRequestURI(), StringUtils.join(paramStringList, "&"), throwable);
    }
}
