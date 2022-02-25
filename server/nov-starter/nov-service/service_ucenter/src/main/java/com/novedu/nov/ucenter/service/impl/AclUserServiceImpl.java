package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.api.RoleType;
import com.novedu.nov.common.config.SysConfigCache;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.ucenter.entity.*;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclPermissionVO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.mapper.AclUserMapper;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.novedu.nov.ucenter.service.AclUserRoleService;
import com.novedu.nov.ucenter.service.AclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements AclUserService {

    @Autowired
    AclUserMapper userMapper;

    @Autowired
    AclUserRoleService userRoleService;

    @Autowired
    AclRoleService roleService;

    @Autowired
    AclPermissionService permissionService;

    @Override
    public BaseResult login(AclUser ucenterMemberDto) {
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        AclUser ucenterMember = query().eq("username", ucenterMemberDto.getUsername())
                .eq("password", password).one();
        if (ucenterMember == null) {
            return BaseResult.error("用户名或密码不正确");
        }
        AclUserRole userRole = userRoleService.query().eq("uid", ucenterMember.getId()).one();
        AclRole role = roleService.query().eq("id",userRole.getRoleId()).one();
        if(role == null || role.getCode() != RoleType.STUDENT.getCode()){
            log.error("uid:" + ucenterMember.getId() + " 当前无权限登录,code:" + role.getCode());
            return BaseResult.error("用户名或密码不正确");
        }
        String token = JwtUtils.createToken(ucenterMember.getId().toString(), ucenterMember.getUsername(), ucenterMember.getNickname(), ucenterMember.getAvatar());
        return BaseResult.success().mapSet("access_token", token);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult register(AclUser ucenterMemberDto) {
        String username = ucenterMemberDto.getUsername();
        int count = query().eq("username", username).count();
        if (count > 0)
            return BaseResult.error("用户名存在!");
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        ucenterMemberDto.setPassword(password);
        String nickname = ucenterMemberDto.getNickname();
        if (!StringUtils.hasText(nickname)) {
            StringBuilder sb = new StringBuilder();
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < 6; i++) {
                sb.append(str.charAt(new Random().nextInt(str.length() - 1)));
            }
            ucenterMemberDto.setNickname("学员" + sb);
        } else {
            if (nickname.length() > 15)
                return BaseResult.error("您的昵称太过个性，请换个简短点的吧,15个字符以内");
        }
        ucenterMemberDto.setAvatar(SysConfigCache.getConfigByKey("stu_def_avatar").getConfigValue());
        save(ucenterMemberDto);
        AclUserRole userRole = new AclUserRole();
        userRole.setUid(ucenterMemberDto.getId());
        AclRole role = roleService.query().eq("code", RoleType.STUDENT.getCode()).select("id").one();
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
        return BaseResult.success();
    }

    @Override
    public BaseResult getMemberInfo(Long id) {
        return BaseResult.success(getById(id));
    }

    @Override
    public BaseResult<List<AclUserRoleVO>> queryUserPage(Page page, AclUserRoleDTO user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(user.getNickname()))
            queryWrapper.like("u.nickname", user.getNickname());
        if (StringUtils.hasText(user.getUsername()))
            queryWrapper.like("u.username", user.getUsername());
        if (user.getRoleId() != null)
            queryWrapper.eq("r.id", user.getRoleId());
        Date start = user.getStartTime();
        Date end = user.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("u.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and u.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        return BaseResult.success(userMapper.queryPage(page, queryWrapper));
    }

    @Override
    public BaseResult loginBg(AclUser user) {
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user = query()
                .eq("username", user.getUsername())
                .eq("password", password).one();
        if (user == null)
            return BaseResult.error("用户名或密码不正确");
        AclUserRole userRole = userRoleService.query().eq("uid", user.getId()).one();
        if (userRole == null) {
            log.error("uid:" + user.getId() + " 未分配角色");
            return BaseResult.error("用户名或密码不正确");
        }
        Integer code = roleService.query().eq("id", userRole.getRoleId()).one().getCode();
        if (code != RoleType.ADMIN.getCode() && code != RoleType.TEACHER.getCode()) {
            log.error("uid:" + user.getId() + " 当前无权限登录,code:" + code);
            return BaseResult.error("用户名或密码不正确");
        }

        String token = JwtUtils.createToken(user.getId().toString(), user.getUsername(), "", "");
        return BaseResult.success("登录成功")
                .mapSet("token", token)
               ;
    }

    @Override
    public BaseResult getInfoBg(String token) {
        String uid = JwtUtils.getAudience(token).get("uid");
        AclUser user = getById(uid);
        if (user == null)
            return BaseResult.error();
        AclUserRole userRole = userRoleService.query().eq("uid", user.getId()).one();
        BaseResult baseResult = permissionService.queryPermissionByRoleId(userRole.getRoleId());
        List<AclPermission> permissions = null;
        if (baseResult != null)
            permissions = (List<AclPermission>) baseResult.getData();
        List<AclPermissionVO> permissionVOS = new ArrayList<>();
        for (AclPermission permission : permissions) {
            if(permission.getType() != 1)
                continue;
            AclPermissionVO aclPermissionVO = new AclPermissionVO();
            BeanUtils.copyProperties(permission,aclPermissionVO);
            if(permission.getStatus() == 2)
                aclPermissionVO.setHidden(true);
            if(StringUtils.hasText(permission.getTitle())){
                Map map = new HashMap<>();
                map.put("title",permission.getTitle());
                map.put("icon",permission.getIcon());
                aclPermissionVO.setMeta(map);
            }
            permissionVOS.add(aclPermissionVO);
        }
        permissionVOS= (List<AclPermissionVO>) TreeUtils.toTree(permissionVOS,AclPermissionVO.class);
        return BaseResult.success()
                .mapSet("username", user.getUsername())
                .mapSet("avatar", user.getAvatar())
                .mapSet("menus", permissionVOS);
    }
}
