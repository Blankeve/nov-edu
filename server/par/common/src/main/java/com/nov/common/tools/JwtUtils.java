package com.nov.common.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：juam
 * @date ：2021/11/24 9:31
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
public class JwtUtils {
    private static final String SECRET_KEY = "FUCK";

    public static String createToken(String userId, String userName) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        Date expireDate = now.getTime();

        return JWT.create()
                .withAudience(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .withClaim("uid", userId)
                .withClaim("username", userName)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static Map getAudience(String token) {
        Map <String,String>userInfo =new HashMap();
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            userInfo.put("uid",decodedJWT.getClaim("uid").asString());
            userInfo.put("username",decodedJWT.getClaim("username").asString());
        } catch (Exception e) {
            userInfo = null;
        }
        return userInfo;
    }

    public static boolean verifyToken(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            log.info("token验证失败");
            return false;
        }
        return true;
    }
}
