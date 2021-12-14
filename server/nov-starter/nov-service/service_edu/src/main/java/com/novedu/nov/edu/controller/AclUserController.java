package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.AclUser;
import com.novedu.nov.edu.service.AclUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-13
 */
@RestController
@RequestMapping("/user")
public class AclUserController {

    @Autowired
    AclUserService aclUserService;

    @PostMapping("/login")
    public BaseResult login(@RequestBody  AclUser user){
        return aclUserService.login(user);

    }
    @GetMapping("/info")
    public BaseResult getInfo(String token){
        return aclUserService.getInfo(token);
    }
}

