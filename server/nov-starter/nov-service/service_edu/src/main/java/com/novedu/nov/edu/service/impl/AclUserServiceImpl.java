package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.edu.entity.AclUser;
import com.novedu.nov.edu.mapper.AclUserMapper;
import com.novedu.nov.edu.service.AclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-13
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements AclUserService {

    @Override
    public BaseResult login(AclUser user) {
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user = query()
                .eq("username", user.getUsername())
                .eq("password", password).one();
        if (user == null)
            return BaseResult.error("用户名或密码不正确");
        String token = JwtUtils.createToken(user.getId().toString(), user.getUsername());
        return BaseResult.success("登录成功").mapSet("token", token);
    }

    @Override
    public BaseResult getInfo(String token) {
        String uid = JwtUtils.getAudience(token).get("uid");
        AclUser user = getById(uid);
        return BaseResult.success()
                .mapSet("username", user.getUsername())
                .mapSet("avatar", user.getAvatar());

    }

}
