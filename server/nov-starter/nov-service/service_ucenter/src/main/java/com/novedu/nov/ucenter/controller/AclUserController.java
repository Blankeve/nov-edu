package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.code.kaptcha.Producer;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.AuthConstant;
import com.novedu.nov.common.constants.Constants;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.util.Base64Utils;
import com.novedu.nov.system.entity.SysConfig;
import com.novedu.nov.system.service.SysConfigService;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.entity.AclUserRole;
import com.novedu.nov.ucenter.entity.dto.AclUserDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserPasswordDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserProfileDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.novedu.nov.ucenter.service.AclUserRoleService;
import com.novedu.nov.ucenter.service.AclUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/ucenter/member/")
public class AclUserController {

    @Autowired
    private AclUserService aclUserService;
    @Autowired
    private SysConfigService configService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AclUserRoleService userRoleService;
    @Autowired
    private AclRoleService roleService;

    @PostMapping("/login")
    public BaseResult login(AclUser aclUser) {
        return aclUserService.login(aclUser);
    }

    @PostMapping("/register")
    public BaseResult register(@Validated AclUser aclUser) {
        return aclUserService.register(aclUser);
    }

    @PostMapping("/info/{id}")
    public BaseResult getMemberInfo(@PathVariable Long id) {
        return aclUserService.getMemberInfo(id);
    }

    @PutMapping("/reset-pwd/{uid}")
    public BaseResult resetPwd(@PathVariable Long uid) {
        return aclUserService.resetPwd(uid);
    }

    @PutMapping("/pwd")
    public BaseResult resetPwd(@Validated @RequestBody AclUserPasswordDTO userPasswordDto) {
        return aclUserService.updatePassword(userPasswordDto);
    }

    @PutMapping("/profile")
    public BaseResult updateProfile(@Validated @RequestBody AclUserProfileDTO userProfileDto) {
        return aclUserService.updateProfile(userProfileDto);
    }

    @GetMapping("/page")
    public BaseResult<List<AclUserRoleVO>> queryUserPage(Page page, AclUserRoleDTO user) {
        return aclUserService.queryUserPage(page, user);
    }

    @PostMapping("/export")
    public void exportUserPage(HttpServletResponse response, Page page, AclUserRoleDTO user) {
        aclUserService.exportUserPage(response, page, user);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response) {
        aclUserService.exportAll(response);
    }

    @PostMapping("/login-bg")
    public BaseResult loginBg(@RequestBody AclUserDTO user) {
        return aclUserService.loginBg(user);

    }

    @PostMapping("/load-username")
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO loadUserByUsername(String username) {
        AclUser aclUser = aclUserService.lambdaQuery().eq(AclUser::getUsername,username).one();
        AclUserRole userRole = userRoleService.lambdaQuery().eq(AclUserRole::getUid,aclUser.getId()).one();
        AclRole role = roleService.lambdaQuery().eq(AclRole::getId,userRole.getRoleId()).one();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(aclUser, userDTO);
        userDTO.setUid(aclUser.getId());
        userDTO.setRoleCode(role.getCode());
        List<String> roles = new ArrayList<>();
        roles.add(role.getId()+"_"+role.getName());
        userDTO.setRoles(roles);
        return userDTO;
    }

    @GetMapping("/info-bg")
    public BaseResult getInfoBg(String token) {
        return aclUserService.getInfoBg(token);
    }

    @GetMapping("/info-client")
    public BaseResult getInfoClient(String token) {
        return aclUserService.getInfoClient(token);
    }

    @GetMapping("/info-dashboard")
    public BaseResult getDashBoardInfo() {
        return aclUserService.getDashBoardInfo();
    }

    @ApiOperation("同步每天用户新增注册和登录人数")
    @GetMapping("/sync-register-login")
    public BaseResult syncRegisterLoginCount() {
        return aclUserService.syncRegisterLoginCount();
    }

    @ApiOperation("同步所有用户至redis缓存")
    @GetMapping("/sync-users-cache")
    public BaseResult syncUsersCache() {
        return aclUserService.syncUsersCache();
    }

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    /**
     * 生成验证码
     */
    @GetMapping("/picVerifyCode")
    public BaseResult getCode(HttpServletResponse response) throws IOException {

        BaseResult<SysConfig> baseResult = configService.getSysConfigByKey(Constants.PIC_VERIFY_CODE);

        SysConfig sysConfig = baseResult.getData();
        if (sysConfig == null) {
            return BaseResult.success();
        }
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString();
        String verifyKey = Constants.PIC_VERIFY_CODE + uuid;

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

        redisTemplate.opsForValue().set(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResult.error(e.getMessage());
        }
        return BaseResult.success().mapSet("uuid", uuid).mapSet("img", Base64Utils.encode(os.toByteArray()));
    }
}

