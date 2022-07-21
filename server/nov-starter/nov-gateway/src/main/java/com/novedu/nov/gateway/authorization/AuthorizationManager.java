package com.novedu.nov.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.novedu.nov.common.base.ResultCode;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.exception.MultiDeviceLoginException;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.gateway.config.IgnoreUrlsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by macro on 2020/6/19.
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    private UserDTO getUserInfo(String token) {
        String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(realToken);
        } catch (ParseException e) {
            return null;
        }
        String userStr = jwsObject.getPayload().toString();
        UserDTO userDto = JSONUtil.toBean(userStr, UserDTO.class);
        return userDto;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        //对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }
        //不同用户体系登录不允许互相访问
        String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }
//        UserDTO userDto = getUserInfo(token);
//        if (AuthConstant.ADMIN_CLIENT_ID.equals(userDto.getClientId()) && !pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
//            return Mono.just(new AuthorizationDecision(false));
//        }
//        if (AuthConstant.PORTAL_CLIENT_ID.equals(userDto.getClientId()) && pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
//            return Mono.just(new AuthorizationDecision(false));
//        }
        UserDTO userDto = getUserInfo(token);
        if (userDto == null) {
            throw new AccessDeniedException("");
        }
        //限制单设备登录账号
        if (!isSingleDevice(token)) {
            throw new MultiDeviceLoginException();
        }
//        //管理员直接放行
//        if (AuthConstant.ADMIN_ROLE_CODE == userDto.getRoleCode()) {
//            return Mono.just(new AuthorizationDecision(true));
//        }
        String clientId = RequestUtils.getClientId(token);
        //非管理端路径直接放行
        if (clientId.equals(AuthConstant.PC_CLIENT_ID)) {
            return Mono.just(new AuthorizationDecision(true));
        }
//        if (!pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
//            return Mono.just(new AuthorizationDecision(true));
//        }
        //非管理员路径需校验权限
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
            if (pathMatcher.match(pattern, uri.getPath())) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }
        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));

    }

    private boolean isSingleDevice(String token) {
        UserDTO userDTO = getUserInfo(token);
        String loginKey = "bg_" + userDTO.getUid();
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
