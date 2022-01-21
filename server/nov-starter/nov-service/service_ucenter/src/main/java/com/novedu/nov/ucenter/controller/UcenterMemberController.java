package com.novedu.nov.ucenter.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.UcenterMember;
import com.novedu.nov.ucenter.entity.dto.UcenterMemberDto;
import com.novedu.nov.ucenter.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/ucenter/member/")
public class UcenterMemberController {

    @Autowired
    UcenterMemberService ucenterMemberService;

    @PostMapping("/login")
    public BaseResult login(UcenterMember ucenterMemberDto){
        return ucenterMemberService.login(ucenterMemberDto);
    }

    @PostMapping("/register")
    public BaseResult register(UcenterMember ucenterMemberDto){
        return ucenterMemberService.register(ucenterMemberDto);
    }
}

