package com.novedu.nov.ucenter.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.service.AclUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    public BaseResult login(AclUser aclUser){
        return aclUserService.login(aclUser);
    }

    @PostMapping("/register")
    public BaseResult register(@Validated AclUser aclUser){
        return aclUserService.register(aclUser);
    }

    @PostMapping("/info/{id}")
    public BaseResult getMemberInfo(@PathVariable Long id){
        return aclUserService.getMemberInfo(id);
    }
}

