package com.novedu.nov.edu.controller;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：juam
 * @date ：2021/12/9 10:52
 * @description：
 * @modified By：
 * @version:
 */
@RequestMapping("/user")
@RestController
public class EduLoginController {

    @PostMapping("/login")
    public BaseResult login(){
        String token = JwtUtils.createToken("test","admin");
        return BaseResult.success("登录成功").map("token",token);
    }
    @GetMapping("/info")
    public BaseResult getInfo(){
        return BaseResult.success().
                map("name","张三");
    }

}
