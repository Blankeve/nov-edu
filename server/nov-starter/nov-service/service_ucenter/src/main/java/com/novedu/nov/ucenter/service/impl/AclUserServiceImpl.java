package com.novedu.nov.ucenter.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.config.SysConfigCache;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.ucenter.entity.AclUser;
import com.novedu.nov.ucenter.mapper.AclUserMapper;
import com.novedu.nov.ucenter.service.AclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Random;

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

    @Override
    public BaseResult login(AclUser ucenterMemberDto) {
        String password = DigestUtils.md5DigestAsHex(ucenterMemberDto.getPassword().getBytes());
        AclUser ucenterMember = query().eq("username", ucenterMemberDto.getUsername())
                .eq("password", password).one();
        if (ucenterMember == null) {
            return BaseResult.error();
        }
        String token = JwtUtils.createToken(ucenterMember.getId().toString(), ucenterMember.getUsername(), ucenterMember.getNickname(), ucenterMember.getAvatar());
        return BaseResult.success().mapSet("access_token", token);
    }

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
        }else {
            if(nickname.length() > 15)
                return BaseResult.error("您的昵称太过个性，请换个简短点的吧,15个字符以内");
        }
        ucenterMemberDto.setAvatar(SysConfigCache.getConfigByKey("stu_def_avatar").getConfigValue());
        return BaseResult.successOrError(save(ucenterMemberDto));
    }

    @Override
    public BaseResult getMemberInfo(Long id) {
        return BaseResult.success(getById(id));
    }
}
