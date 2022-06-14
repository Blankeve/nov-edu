package com.novedu.nov.common.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    private static String tokenKey = "X-Token";

    public static Long getUid() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader(tokenKey);
        if(StringUtils.isEmpty(token))
            return null;
        return Long.valueOf(JwtUtils.getAudience(token).get("uid"));
    }
}
