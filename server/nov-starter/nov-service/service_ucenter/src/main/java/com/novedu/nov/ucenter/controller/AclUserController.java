package com.novedu.nov.ucenter.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.entity.dto.AclUserPasswordDto;
import com.novedu.nov.ucenter.entity.dto.AclUserProfileDto;
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
    AclUserService aclUserService;

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
    public BaseResult resetPwd(@Validated @RequestBody AclUserPasswordDto userPasswordDto) {
        return aclUserService.updatePassword(userPasswordDto);
    }

    @PutMapping("/profile")
    public BaseResult updateProfile(@Validated @RequestBody AclUserProfileDto userProfileDto) {
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
    public BaseResult loginBg(@RequestBody AclUser user) {
        return aclUserService.loginBg(user);

    }

    @GetMapping("/info-bg")
    public BaseResult getInfoBg(String token) {
        return aclUserService.getInfoBg(token);
    }

    @GetMapping("/info-dashboard")
    public BaseResult getDashBoardInfo() {
        return aclUserService.getDashBoardInfo();
    }

    @ApiOperation("同步每天用户新增注册和登录人数")
    @GetMapping("/sync-register-login")
    public BaseResult syncRegisterLoginCount(){
        return aclUserService.syncRegisterLoginCount();
    }
}

