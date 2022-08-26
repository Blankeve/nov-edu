package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.entity.dto.AclUserDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserPasswordDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserProfileDTO;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import com.novedu.nov.ucenter.service.AclUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PutMapping("/reset-pwd/{uids}")
    public BaseResult resetPwd(@PathVariable Long[] uids) {
        return aclUserService.resetPwd(uids);
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
        return BaseResult.success(aclUserService.queryUserPage(page, user));
    }

    @GetMapping("/export")
    public void exportUserPage(HttpServletResponse response, AclUserRoleDTO user) {
        ExcelUtils.exportExcel(aclUserService.queryUserPage(new Page(1, -1), user).getRecords(), "用户信息", "用户信息", AclUserRoleVO.class, "用户信息", response);
    }


    @PostMapping("/login-bg")
    public BaseResult loginBg(@RequestBody AclUserDTO user) {
        return aclUserService.loginBg(user);

    }

    @PostMapping("/load-username")
    public UserDTO loadUserByUsername(String username) {
        return aclUserService.loadUserByUsername(username);
    }

    @GetMapping("/info-bg")
    public BaseResult getInfoBg(String token) {
        return aclUserService.getInfoBg(token);
    }

    @GetMapping("/info-client")
    public BaseResult getInfoClient(String token) {
        return aclUserService.getInfoClient(token);
    }

    @GetMapping("/login-info")
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

    /**
     * 生成验证码
     */
    @GetMapping("/picVerifyCode")
    public BaseResult getCode() {
        return aclUserService.getCode();
    }
}

