package com.novedu.nov.ucenter.component;

import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.constants.Constants;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.entity.AclRolePermission;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.novedu.nov.ucenter.service.AclRolePermissionService;
import com.novedu.nov.ucenter.service.AclRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
@Slf4j
@Component
public class InitRolePermissionHandler {

    @Autowired
    private AclRoleService roleService;
    @Autowired
    private AclRolePermissionService rolePermissionService;
    @Autowired
    private AclPermissionService permissionService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void InitRolePermission() {
        Map<String, List<String>> resourceRoleMap = new TreeMap<>();
        List<AclPermission> permissionList = permissionService.lambdaQuery().eq(AclPermission::getType, AuthConstant.BUTTON_PERMISSION_TYPE).list();
        List<AclRole> roleList = roleService.list();
        List<AclRolePermission> rolePermissionList = rolePermissionService.list();
        for (AclPermission permission : permissionList) {
            Set<Long> roleIds = rolePermissionList.stream().filter(item -> item.getPermissionId().equals(permission.getId())).map(AclRolePermission::getRoleId).collect(Collectors.toSet());
            List<String> roleNames = roleList.stream().filter(item -> roleIds.contains(item.getId())).map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
            resourceRoleMap.put(permission.getValue(),roleNames);
        }
        redisTemplate.delete(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        redisTemplate.opsForHash().putAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRoleMap);
        ;
        log.info("需要鉴权接口个数:"+resourceRoleMap.size());
    }
}
