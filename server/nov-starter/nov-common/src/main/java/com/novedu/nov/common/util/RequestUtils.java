package com.novedu.nov.common.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    private static String tokenKey = "X-Token";
    private static HttpServletRequest request;

    private static void getRequest() {
        request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static Long getUid() {
        getRequest();
        String token = request.getHeader(tokenKey);
        if (StringUtils.isEmpty(token))
            return null;
        return Long.valueOf(JwtUtils.getAudience(token).get("uid"));
    }

    public static String getUsername() {
        getRequest();
        String token = request.getHeader(tokenKey);
        if (StringUtils.isEmpty(token))
            return "";
        return JwtUtils.getAudience(token).get("username");
    }
}
