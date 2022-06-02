package com.novedu.nov.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.api.ResultCode;
import com.novedu.nov.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    private String[] whiteList = new String[]{"/order/trade/hasbuy", "/order/trade/count", "/statistics/daily/register-and-access",
            "/ucenter/role/by-uid", "/ucenter/member/login", "/ucenter/member/register", "/ucenter/member/login-bg", "/ucenter/member/dashboard-recent-users", "/edu/course/detail", "/edu/course-apply/save", "/edu/course-apply/already",
            "/edu/course/statistics/course/applyCount", "/edu/course/statistics/course/buyCount", "/edu/edu-teacher/bind", "/ucenter/member/info", "/ucenter/role/by-uid",
            "/edu/course/statistics/course/playCount", "/edu/course/statistics/course/applyCount", "/edu/course/statistics/course/buyCount", "/edu/edu-teacher/clear-bind", "/edu/banner/client-list",
            "/edu/course/client-list", "/edu/edu-teacher/client-list", "/edu/notice/receive", "/edu/course/client-tree", "/edu/comment/page-client", "/edu/course/list-teacher", "/edu/edu-teacher/info", "/edu/course/page-client",
            "/edu/subject/list", "/edu/edu-teacher/list", "/ucenter/member/sync-register-login", "/order/alipay","img","video","error","favicon.ico","img","video","/edu/consult/page-client","/edu/info/page","/ucenter/member/sync-users-cache",
            "/edu/config/list","/edu/info/detail-client"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        String url = request.getRequestURI();
        if (isWhiteList(url))
            return true;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (!StringUtils.hasText(token)) {
            response.getWriter().append(new ObjectMapper().writeValueAsString(BaseResult.setStatus(ResultCode.LOGIN_FAIL)));
            return false;
        } else if (!JwtUtils.verifyToken(token)) {
            response.getWriter().append(new ObjectMapper().writeValueAsString(BaseResult.setStatus(ResultCode.LOGIN_FAIL)));
            return false;
        } else if (!isSingleDevice(token)) {
            response.getWriter().append(new ObjectMapper().writeValueAsString(BaseResult.setStatus(ResultCode.OTHER_DEVICE_LOGIN)));
            return false;
        }
        return true;
    }


    private boolean isWhiteList(String url) {
        for (int i = 0; i < whiteList.length; i++) {
            if (url.indexOf(whiteList[i]) > -1)
                    return true;
        }
        log.info("not white list:"+url);
        return false;
    }

    private boolean isSingleDevice(String token) {
        String uid = JwtUtils.getAudience(token).get("uid");
        String loginKey = "bg_" + uid;
        if (redisTemplate.hasKey(loginKey)) {
            String redisToken = (String) redisTemplate.opsForValue().get(loginKey);
            if (token.equals(redisToken)) {
                return true;
            } else
                return false;
        } else
            return true;
    }
}
