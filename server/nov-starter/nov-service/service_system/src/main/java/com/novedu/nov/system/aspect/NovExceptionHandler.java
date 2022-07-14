package com.novedu.nov.system.aspect;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author ：juam
 * @date ：2021/11/4 13:52
 * @description：自定义全局异常处理器
 * @modified By：
 * @version:
 */
@Slf4j
@RestControllerAdvice
public class NovExceptionHandler {
    @ExceptionHandler(value = BindException.class)
    public BaseResult exceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()
        ) {
            sb.append("[")
                    .append(fieldError.getDefaultMessage())
                    .append("]")
            ;
        }
        log.error(sb.toString());
        //4.返回字段校验异常信息给接口调用方
        return BaseResult.error(sb.toString());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResult exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()
        ) {
            sb.append("[")
                    .append(fieldError.getDefaultMessage())
                    .append("]")
            ;
        }
        log.error(sb.toString());
        //4.返回字段校验异常信息给接口调用方
        return BaseResult.error(sb.toString());
    }

    @ExceptionHandler(value = ServiceInvokeFailureException.class)
    public BaseResult exceptionHandler(ServiceInvokeFailureException e) {
        //1.获取字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //2.打印异常到输出流
        e.printStackTrace(new PrintStream(byteArrayOutputStream));
        //3.使用logback输出异常信息至控制台并保存到本地文件
        log.error(byteArrayOutputStream.toString());
        return BaseResult.error("网络出差了，请稍后再试");
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResult exceptionHandler(Exception e) {
        //1.获取字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //2.打印异常到输出流
        e.printStackTrace(new PrintStream(byteArrayOutputStream));
        //3.使用logback输出异常信息至控制台并保存到本地文件
        log.error(byteArrayOutputStream.toString());
        return BaseResult.error("出错了，请联系管理员");
    }
}
