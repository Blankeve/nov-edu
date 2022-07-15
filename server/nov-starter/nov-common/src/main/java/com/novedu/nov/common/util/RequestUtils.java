package com.novedu.nov.common.util;

import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.entity.UserDTO;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

public class RequestUtils {
    private static HttpServletRequest request;

    private static void getRequest() {
        request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static UserDTO getUserInfo() {
        getRequest();
        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(realToken);
        } catch (ParseException e) {
            return null;
        }
        String userStr = jwsObject.getPayload().toString();
        if (StringUtils.isEmpty(token))
            return null;
        UserDTO userDto = JSONUtil.toBean(userStr, UserDTO.class);
        return userDto;
    }

    public static Long getUid() {
        try {
            return getUserInfo().getUid();
        }
        catch (NullPointerException e){
            return null;
        }
    }

    public static Integer getRoleCode() {
        try {
            return getUserInfo().getRoleCode();
        }
        catch (NullPointerException e){
            return null;
        }
    }

    public static String getUsername() {
        try {
            return getUserInfo().getUsername();
        }
      catch (NullPointerException e){
            return "";
      }
    }
}
