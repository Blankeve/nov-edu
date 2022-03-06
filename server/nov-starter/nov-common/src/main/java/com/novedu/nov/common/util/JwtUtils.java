package com.novedu.nov.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.models.auth.In;

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
public class JwtUtils {
    private static final String SECRET_KEY = "FUCK";

    public static String createToken(String userId, String username , String rolecode) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        Date expireDate = now.getTime();

        return JWT.create()
                .withAudience(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .withClaim("uid", userId)
                .withClaim("username", username)
                .withClaim("rolecode", rolecode)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static Map<String, String> getAudience(String token) {
        Map<String, String> userInfo = new HashMap<>();
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            userInfo.put("username", decodedJWT.getClaim("username").asString());
            userInfo.put("uid", decodedJWT.getClaim("uid").asString());
            userInfo.put("rolecode", decodedJWT.getClaim("rolecode").asString());
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
            return false;
        }
        return true;
    }
}
