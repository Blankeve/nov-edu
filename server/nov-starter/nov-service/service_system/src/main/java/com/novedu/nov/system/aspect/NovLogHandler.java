package com.novedu.nov.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.helper.SnowFlake;
import com.novedu.nov.common.util.IpAddressUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.system.entity.SysOperLog;
import com.novedu.nov.system.service.SysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
public class NovLogHandler {

    private static List<SysOperLog> sysOperLogs = new ArrayList<>();
    @Autowired
    SysOperLogService sysOperLogService;

    @Pointcut("execution(* com.novedu.nov.edu.controller.*.*(..))" +
            " || execution(* com.novedu.nov.ucenter.controller.*.*(..))" +
            " || execution(* com.novedu.nov.statistics.controller.*.*(..))" +
            " || execution(* com.novedu.nov.upload.controller.*.*(..))" +
            " || execution(* com.novedu.nov.order.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setReqTime(Calendar.getInstance().getTime());
        String reqUrl = request.getRequestURI();
        String reqMethod = request.getMethod();
        String ip = IpAddressUtils.getIpAddress(request);
        SnowFlake snowFlake = new SnowFlake();
        Long id = snowFlake.nextValue();
        String username = RequestUtils.getUsername();
        String addr = IpAddressUtils.getRealAddressByIP(ip);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String clazzName = joinPoint.getTarget().getClass().getName();
        String[][] params = getParamsName(method);
        StringBuilder args = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            args.append(String.format("\n\t%s %s:%s\t", params[i][0], params[i][1], joinPoint.getArgs()[i]));
        }
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long spendTime = System.currentTimeMillis() - startTime;
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        sysOperLog.setId(id);
        if (!StringUtils.hasText(username)) {
            if ("内网IP".equals(addr))
                sysOperLog.setOperName("内部人员");
            else
                sysOperLog.setOperName("访客");
        } else
            sysOperLog.setOperName(username);
        sysOperLog.setOperIp(ip);
        sysOperLog.setOperAddr(addr);
        sysOperLog.setReqUrl(reqUrl);
        sysOperLog.setReqClass(clazzName);
        sysOperLog.setReqMethod(method.getName());
        sysOperLog.setMethod(reqMethod);
        if (args.toString().length() < 255)
            sysOperLog.setReqArgs(args.toString());
        else
            sysOperLog.setReqArgs("请求参数过长，取消显示");
        if (json.length() < 255)
            sysOperLog.setReqResult(json);
        else
            sysOperLog.setReqResult("响应结果太长，取消显示");
        sysOperLog.setReqTimeSpend(spendTime);
        sysOperLogs.add(sysOperLog);
        if (sysOperLogs.size() > 10) {
            try {
                sysOperLogService.saveBatch(sysOperLogs);
            } catch (Exception e) {
            } finally {
                sysOperLogs.clear();
            }

        }
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
