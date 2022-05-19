package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.api.RoleType;
import com.novedu.nov.common.config.SysConfigCache;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.IpAddressUtils;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.ucenter.entity.*;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclPermissionVO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.mapper.AclUserMapper;
import com.novedu.nov.ucenter.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SysLoginHistoryService sysLoginHistoryService;

    @Override
    public BaseResult login(AclUser ucenterMemberDto) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        AclUser ucenterMember = query().eq("username", ucenterMemberDto.getUsername())
                .eq("password", password).one();
        if (ucenterMember == null) {
            return BaseResult.error("用户名或密码不正确");
        }
        AclUserRole userRole = userRoleService.query().eq("uid", ucenterMember.getId()).one();
        AclRole role = roleService.query().eq("id", userRole.getRoleId()).one();
        if (role == null || role.getCode() != RoleType.STUDENT.getCode()) {
            log.error("uid:" + ucenterMember.getId() + " 当前无权限登录,code:" + role.getCode());
            return BaseResult.error("用户名或密码不正确");
        }
        saveLoginInfo(ucenterMemberDto);
        String token = JwtUtils.createToken(ucenterMember.getId().toString(), ucenterMember.getUsername(), RoleType.STUDENT.getCode() + "");
        Map loginInfo = new HashMap();
        loginInfo.put("nickname", ucenterMember.getNickname());
        loginInfo.put("avatar", ucenterMember.getAvatar());
        return BaseResult.success().mapSet("access_token", token).mapSet("loginInfo", loginInfo);
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

    public LoginInfo getLoginInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        String ip = IpAddressUtils.getIpAddress(request);
        String location = IpAddressUtils.getRealAddressByIP(ip);
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setOS(os);
        loginInfo.setDevice(browser);
        loginInfo.setIp(ip);
        loginInfo.setLocation(location);
        return loginInfo;
    }

    private void saveLoginInfo(AclUser user){
        LoginInfo loginInfo = getLoginInfo();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", user.getId());
        updateWrapper.set("last_login_time", new Date());
        updateWrapper.set("last_login_ip", loginInfo.getLocation());
        update(updateWrapper);
        SysLoginHistory sysLoginHistory = new SysLoginHistory();
        sysLoginHistory.setUsername(user.getUsername());
        sysLoginHistory.setLoginIp(loginInfo.getIp());
        sysLoginHistory.setLoginAddress(loginInfo.getLocation());
        sysLoginHistory.setLoginDevice(loginInfo.getOS()+" "+loginInfo.getDevice());
        sysLoginHistoryService.save(sysLoginHistory);
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
        if (code == RoleType.STUDENT.getCode()) {
            log.error("uid:" + user.getId() + " 当前无权限登录,code:" + code);
            return BaseResult.error("用户名或密码不正确");
        }
        String token = JwtUtils.createToken(user.getId().toString(), user.getUsername(), code.toString());
        String loginKey = "bg_" + user.getId();
        redisTemplate.opsForValue().set(loginKey, token, 1, TimeUnit.DAYS);
        saveLoginInfo(user);
        return BaseResult.success("登录成功")
                .mapSet("token", token)
                ;
    }

    @Override
    public BaseResult getInfoBg(String token) {
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        AclUser user = getById(uid);
        if (user == null)
            return BaseResult.error();
        AclUserRole userRole = userRoleService.query().eq("uid", user.getId()).one();
        AclRole role = roleService.query().eq("id", userRole.getRoleId()).one();
        BaseResult baseResult = permissionService.queryPermissionByRoleId(userRole.getRoleId());
        List<AclPermission> permissions = null;
        if (baseResult != null)
            permissions = (List<AclPermission>) baseResult.getData();
        List<AclPermissionVO> permissionVOS = new ArrayList<>();
        for (AclPermission permission : permissions) {
            if (permission.getType() != 1)
                continue;
            AclPermissionVO aclPermissionVO = new AclPermissionVO();
            BeanUtils.copyProperties(permission, aclPermissionVO);
            if (permission.getStatus() == 2)
                aclPermissionVO.setHidden(true);
            if (StringUtils.hasText(permission.getTitle())) {
                Map map = new HashMap<>();
                map.put("title", permission.getTitle());
                map.put("icon", permission.getIcon());
                aclPermissionVO.setMeta(map);
            }
            permissionVOS.add(aclPermissionVO);
        }
        permissionVOS = (List<AclPermissionVO>) TreeUtils.toTree(permissionVOS, AclPermissionVO.class);
        return BaseResult.success()
                .mapSet("username", user.getUsername())
                .mapSet("avatar", user.getAvatar())
                .mapSet("code", role.getCode())
                .mapSet("roleName", role.getName())
                .mapSet("menus", permissionVOS);
    }

    @Override
    public BaseResult resetPwd(Long uid) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", uid);
        updateWrapper.set("password", DigestUtils.md5DigestAsHex(SysConfigCache.getConfigByKey("user_def_reset_pwd").getConfigValue().getBytes(StandardCharsets.UTF_8)));
        return BaseResult.successOrError(update(updateWrapper));
    }

    @Override
    public void exportUserPage(HttpServletResponse response, Page page, AclUserRoleDTO user) {
        BaseResult baseResult = queryUserPage(page, user);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "用户信息", "用户信息", AclUserRoleVO.class, "用户信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response) {
        BaseResult baseResult = queryUserPage(new Page(1, count()), new AclUserRoleDTO());
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "用户信息", "用户信息", AclUserRoleVO.class, "用户信息", response);
        }
    }

    private List getRecentAddUsers() {
        List list = query().orderByDesc("create_time").list();
        if (list.size() > 3)
            list = list.subList(0, 3);
        return list;
    }

    @Override
    public BaseResult getDashBoardInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("X-Token");
        Long uid = Long.valueOf(JwtUtils.getAudience(token).get("uid"));
        AclUser user = getById(uid);
        if (user == null)
            return BaseResult.error();
        AclUserRole userRole = userRoleService.query().eq("uid", user.getId()).one();
        AclRole role = roleService.query().eq("id", userRole.getRoleId()).one();
        Map userInfo = new HashMap();
        userInfo.put("uid", user.getId());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("username", user.getUsername());
        userInfo.put("rolename", role.getName());
        userInfo.put("code", role.getCode());
        userInfo.put("lastLoginTime", user.getLastLoginTime());
        userInfo.put("lastLoginIp", user.getLastLoginIp());
        if (!role.getCode().equals(RoleType.TEACHER.getCode())) {
            userInfo.put("users", count());
            userInfo.put("recentAddUsers", getRecentAddUsers());
            String key = "access_num";
            if (redisTemplate.hasKey(key)) {
                Integer accessNum = (Integer) redisTemplate.opsForValue().get(key) / 2;
                userInfo.put("accessNum", accessNum);
            }
        }
        return BaseResult.success().mapSet("userInfo", userInfo);
    }

    @Override
    public BaseResult syncRegisterLoginCount() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String nowDate = formatter.format(date);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("create_time", nowDate);
        Integer registerCount = count(queryWrapper);
        queryWrapper = new QueryWrapper();
        queryWrapper.like("last_login_time", nowDate);
        Integer loginCount = count(queryWrapper);
        return BaseResult.success().mapSet("registerCount", registerCount).mapSet("loginCount", loginCount);
    }
}
