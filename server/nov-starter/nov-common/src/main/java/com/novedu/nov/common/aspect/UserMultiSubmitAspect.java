package com.novedu.nov.common.aspect;

import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.util.IpAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class UserMultiSubmitAspect {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 是否存在注解，如果存在就获取
     */
    private UserMultiSubmitLimit getAnnotationLog(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (method != null) {
            return method.getAnnotation(UserMultiSubmitLimit.class);
        }
        return null;
    }


    @Pointcut("execution(* com.novedu.nov.*.controller.*.*(..))")
    public void pointcut() {
    }

    /**
     * @param point
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        UserMultiSubmitLimit avoidRepeatableCommit = getAnnotationLog(point);

        if (avoidRepeatableCommit == null)
            return point.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = IpAddressUtils.getIpAddress(request);

        //目标类、方法
        String className = method.getDeclaringClass().getName();

        String name = method.getName();

        // 得到类名和方法
        String ipKey = String.format("%s#%s", className, name);

        // 转换成HashCode
        int hashCode = Math.abs(ipKey.hashCode());

        String key = String.format("%s:%s_%d", "AVOID_REPEATABLE_COMMIT", ip, hashCode);

        log.info("ipKey={},hashCode={},key={}", ipKey, hashCode, key);

        long timeout = avoidRepeatableCommit.timeout();

        String value = (String) redisTemplate.opsForValue().get(key);

        if (!StringUtils.isEmpty(value)) {
            log.info("请勿重复提交表单");
            return BaseResult.error("提交过快，请稍后再试");
        }

        // 设置过期时间
        redisTemplate.opsForValue().set(key, UUID.randomUUID().toString(), timeout, TimeUnit.MILLISECONDS);

        //执行方法
        Object object = point.proceed();
        return object;
    }

}

