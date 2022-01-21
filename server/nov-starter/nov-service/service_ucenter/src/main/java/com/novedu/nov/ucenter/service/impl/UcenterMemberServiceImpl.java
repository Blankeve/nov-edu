package com.novedu.nov.ucenter.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.ucenter.entity.UcenterMember;
import com.novedu.nov.ucenter.mapper.UcenterMemberMapper;
import com.novedu.nov.ucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-01-19
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Override
    public BaseResult login(UcenterMember ucenterMemberDto) {
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        UcenterMember ucenterMember = query().eq("username", ucenterMemberDto.getUsername())
                .eq("password", password).one();
        if(ucenterMember == null){
            return BaseResult.error();
        }
        String token = JwtUtils.createToken(ucenterMember.getId().toString(), ucenterMember.getUsername(),ucenterMember.getNickname(),ucenterMember.getAvatar());
        return BaseResult.success().mapSet("access_token",token);
    }

    @Override
    public BaseResult register(UcenterMember ucenterMemberDto) {
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        ucenterMemberDto.setPassword(password);
        if(!StringUtils.hasText(ucenterMemberDto.getNickname())){
            StringBuilder sb = new StringBuilder();
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < 6; i++) {
                sb.append(str.charAt(new Random().nextInt(str.length() - 1)));
            }
            ucenterMemberDto.setNickname("学员"+sb.toString());
        }
        return BaseResult.successOrError(save(ucenterMemberDto));
    }
}
