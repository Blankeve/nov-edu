package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Producer;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.util.Base64Utils;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.IpAddressUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.system.entity.SysConfig;
import com.novedu.nov.system.entity.SysLoginHistory;
import com.novedu.nov.system.service.SysConfigService;
import com.novedu.nov.system.service.SysLoginHistoryService;
import com.novedu.nov.ucenter.client.OpenAuthService;
import com.novedu.nov.ucenter.entity.*;
import com.novedu.nov.ucenter.entity.dto.AclUserDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserPasswordDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserProfileDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclPermissionVO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.mapper.AclUserMapper;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.novedu.nov.ucenter.service.AclUserRoleService;
import com.novedu.nov.ucenter.service.AclUserService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    @Autowired
    SysConfigService configService;

    @Autowired
    OpenAuthService authService;

    @Override
    public BaseResult login(AclUser ucenterMemberDto) {
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        AclUser ucenterMember = lambdaQuery().eq(AclUser::getUsername, ucenterMemberDto.getUsername())
                .eq(AclUser::getPassword, password).one();
        if (ucenterMember == null) {
            return BaseResult.error("用户名或密码不正确");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.PC_CLIENT_ID);
        params.put("client_secret", "777777");
        params.put("grant_type", "password");
        params.put("username", ucenterMemberDto.getUsername());
        params.put("password", ucenterMemberDto.getPassword());

        try {
            BaseResult baseResult1 = authService.postAccessToken(params);
            if (BaseResult.success().getCode().equals(baseResult1.getCode())) {
                AclUser user2 = lambdaQuery().eq(AclUser::getUsername, ucenterMemberDto.getUsername()).one();
                Map data = (Map) baseResult1.getData();
                String token = AuthConstant.JWT_TOKEN_PREFIX + data.get("token");
                String loginKey = "bg_" + user2.getId();
                redisTemplate.opsForValue().set(loginKey, token, 1, TimeUnit.DAYS);
                saveLoginInfo(user2);
                Map loginInfo = new HashMap();
                loginInfo.put("nickname", ucenterMember.getNickname());
                loginInfo.put("avatar", ucenterMember.getAvatar());
                return BaseResult.success().map("access_token", token).map("loginInfo", loginInfo);
            } else {
                return BaseResult.error(baseResult1.getMsg());
            }
        } catch (Exception e) {
            return BaseResult.error("登录超时，请稍后再试");
        }

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
        ucenterMemberDto.setAvatar(configService.getSysConfigByKey("stu_def_avatar").getData().getConfigValue());
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
        return BaseResult.success(userMapper.getInfoById(id));
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

    public LoginInfo getLoginInfo() {
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

    private void saveLoginInfo(AclUser user) {
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
        sysLoginHistory.setLoginDevice(loginInfo.getOS() + " " + loginInfo.getDevice());
        sysLoginHistoryService.save(sysLoginHistory);
    }

    @Override
    public BaseResult loginBg(AclUserDTO user) {
        BaseResult<SysConfig> baseResult = configService.getSysConfigByKey(RedisKeyConstants.PIC_VERIFY_CODE);
        if (baseResult.getData() != null) {
            String verifyKey = RedisKeyConstants.PIC_VERIFY_CODE + user.getUuid();
            String captcha = (String) redisTemplate.opsForValue().get(verifyKey);
            redisTemplate.delete(verifyKey);
            if (captcha == null) {
                return BaseResult.error("验证码失效");
            }
            if (!captcha.equalsIgnoreCase(user.getCode())) {
                return BaseResult.error("验证码不正确");
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.PC_ADMIN_ID);
        params.put("client_secret", "666666");
        params.put("grant_type", "password");
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        try {
            BaseResult baseResult1 = authService.postAccessToken(params);
            if (BaseResult.success().getCode().equals(baseResult1.getCode())) {
                AclUser user2 = lambdaQuery().eq(AclUser::getUsername, user.getUsername()).one();
                Map data = (Map) baseResult1.getData();
                String token = AuthConstant.JWT_TOKEN_PREFIX + data.get("token");
                String loginKey = "bg_" + user2.getId();
                redisTemplate.opsForValue().set(loginKey, token, 1, TimeUnit.DAYS);
                saveLoginInfo(user2);
                return BaseResult.success("登录成功")
                        .map("token", token)
                        ;
            } else {
                return BaseResult.error(baseResult1.getMsg());
            }
        } catch (Exception e) {
            return BaseResult.error("登录超时，请稍后再试");
        }
    }

    @Override
    public BaseResult getInfoBg(String token) {
        Long uid = RequestUtils.getUid();
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
        if (!CollectionUtils.isEmpty(permissionVOS)) {
            permissionVOS = (List<AclPermissionVO>) TreeUtils.toTree(permissionVOS, AclPermissionVO.class);
            Collections.sort(permissionVOS);
            permissionVOS.forEach(o -> childrenSort(o.getChildren()));
        }
        return BaseResult.success()
                .map("username", user.getUsername())
                .map("avatar", user.getAvatar())
                .map("code", role.getCode())
                .map("roleName", role.getName())
                .map("menus", permissionVOS);
    }

    private void childrenSort(List<AclPermissionVO> children) {
        if (CollectionUtils.isEmpty(children))
            return;
        Collections.sort(children);
        children.forEach(o -> childrenSort(o.getChildren()));
    }

    @Override
    public BaseResult resetPwd(Long uid) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", uid);
        updateWrapper.set("password", DigestUtils.md5DigestAsHex(configService.getSysConfigByKey("user_def_reset_pwd").getData().getConfigValue().getBytes(StandardCharsets.UTF_8)));
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
        Long uid = RequestUtils.getUid();
        AclUser user = getById(uid);
        if (user == null)
            return BaseResult.error();
        AclUserRole userRole = userRoleService.query().eq("uid", user.getId()).one();
        AclRole role = roleService.query().eq("id", userRole.getRoleId()).one();
        Map userInfo = new HashMap();
        userInfo.put("lastLoginTime", user.getLastLoginTime());
        userInfo.put("lastLoginIp", user.getLastLoginIp());
        if (!role.getCode().equals(RoleType.TEACHER.getCode())) {
            userInfo.put("users", count());
            userInfo.put("recentAddUsers", getRecentAddUsers());
            String key = "access_num";
            if (redisTemplate.hasKey(key)) {
                Integer accessNum = (Integer) redisTemplate.opsForValue().get(key);
                userInfo.put("accessNum", accessNum);
            }
        }
        return BaseResult.success().map("userLoginInfo", userInfo);
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
        return BaseResult.success().map("registerCount", registerCount).map("loginCount", loginCount);
    }

    @Override
    public BaseResult updatePassword(AclUserPasswordDTO userPasswordDto) {
        String oldpass = DigestUtils.md5DigestAsHex(userPasswordDto.getOldpass().getBytes(StandardCharsets.UTF_8));
        AclUser aclUser = getById(userPasswordDto.getId());
        if (!oldpass.equals(aclUser.getPassword()))
            return BaseResult.error("修改密码失败，旧密码不正确");
        String newpass = DigestUtils.md5DigestAsHex(userPasswordDto.getNewpass().getBytes(StandardCharsets.UTF_8));
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", userPasswordDto.getId());
        updateWrapper.set("password", newpass);
        update(updateWrapper);
        return BaseResult.success();
    }

    @Override
    public BaseResult updateProfile(AclUserProfileDTO userProfileDto) {
        AclUser aclUser = new AclUser();
        BeanUtils.copyProperties(userProfileDto, aclUser);
        return BaseResult.successOrError(updateById(aclUser));
    }

    @Override
    public BaseResult syncUsersCache() {
        List<AclUser> aclUsers = list();
        if (ObjectUtils.isEmpty(aclUsers))
            return BaseResult.error("获取用户列表失败");
        try {
            redisTemplate.opsForValue().set(RedisKeyConstants.USERS_CACHE, new ObjectMapper().writeValueAsString(aclUsers));
        } catch (JsonProcessingException e) {
            return BaseResult.error("用户列表写入缓存失败");
        }
        return BaseResult.success();
    }

    @Override
    public BaseResult getInfoClient(String token) {
        Long uid = RequestUtils.getUid();
        AclUser user = getById(uid);
        return BaseResult.success().map("username", user.getUsername())
                .map("uid", uid + "")
                .map("nickname", user.getNickname())
                .map("avatar", user.getAvatar());
    }

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Override
    public BaseResult getCode() {
        BaseResult<SysConfig> baseResult = configService.getSysConfigByKey(RedisKeyConstants.PIC_VERIFY_CODE);

        SysConfig sysConfig = baseResult.getData();
        if (sysConfig == null) {
            return BaseResult.success();
        }
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString();
        String verifyKey = RedisKeyConstants.PIC_VERIFY_CODE + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        String codeType = sysConfig.getConfigValue();
        // 生成验证码
        if ("math".equals(codeType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(codeType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisTemplate.opsForValue().set(verifyKey, code, RedisKeyConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.success().map("uuid", uuid).map("img", Base64Utils.encode(os.toByteArray()));
    }

    @Override
    public UserDTO loadUserByUsername(String username) {
        AclUser aclUser = lambdaQuery().eq(AclUser::getUsername, username).one();
        AclUserRole userRole = userRoleService.lambdaQuery().eq(AclUserRole::getUid, aclUser.getId()).one();
        AclRole role = roleService.lambdaQuery().eq(AclRole::getId, userRole.getRoleId()).one();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(aclUser, userDTO);
        userDTO.setUid(aclUser.getId());
        userDTO.setRoleCode(role.getCode());
        List<String> roles = new ArrayList<>();
        roles.add(role.getId() + "_" + role.getName());
        userDTO.setRoles(roles);
        return userDTO;
    }
}
