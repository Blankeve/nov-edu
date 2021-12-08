package com.novedu.nov.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ：juam
 * @date ：2021/12/8 13:22
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@Aspect
@Component
public class LogHandler {
    @Pointcut("execution(* com.novedu.nov.*.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuilder webLog = new StringBuilder();
        webLog.append("\n------------->request start<-------------");
        String reqUrl = request.getRequestURL().toString();
        String reqMethod = request.getMethod();
//        UserInfo userInfo = JwtUtils.getAudience(request.getHeader("token"));
//        String reqUser = userInfo == null ? "" : userInfo.getUsername();
        String reqAddr = request.getRemoteAddr();
        webLog.append("\nURL:" + reqUrl);
        webLog.append("\nmethod:" + reqMethod);
//        webLog.append("\n用户名:" + reqUser);
        webLog.append("\nIP:" + reqAddr);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String clazzName = joinPoint.getTarget().getClass().getName();
        String[][] params = getParamsName(method);
        webLog.append(String.format("\nclass:%s\nmethod:%s\nargs:", clazzName, method.getName()));
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            webLog.append(String.format("\n\t%s %s:%s\t", params[i][0], params[i][1], joinPoint.getArgs()[i]));
        }
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long spendTime = System.currentTimeMillis() - startTime;
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        webLog.append("\nresult:" + json);
        webLog.append("\nspend:" + spendTime + "ms");
        webLog.append("\n------------->request end<-------------\n");
        log.debug(webLog.toString());
        return obj;
    }

    public String[][] getParamsName(Method method) {
        Parameter[] parameters = method.getParameters();
        String[][] params = new String[parameters.length][2];
        for (int i = 0; i < parameters.length; i++) {
            String paramType = parameters[i].getType().getTypeName();
            String[] buffer = paramType.split("[.]");
            if (buffer.length > 0)
                paramType = buffer[buffer.length - 1];
            String paramName = parameters[i].getName();
            params[i][0] = paramType;
            params[i][1] = paramName;
        }
        return params;
    }


}
