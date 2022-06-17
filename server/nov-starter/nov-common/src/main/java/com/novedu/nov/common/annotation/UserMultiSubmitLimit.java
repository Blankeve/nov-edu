package com.novedu.nov.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 避免接口重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserMultiSubmitLimit {
    /**
     * 指定时间内不可重复提交,单位毫秒，默认2秒
     */
    long timeout() default 2000;
}

