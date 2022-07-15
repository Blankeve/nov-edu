package com.novedu.nov.auth.client;

import com.novedu.nov.common.entity.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenUserServiceImpl implements OpenUserService {
    @Override
    public UserDTO loadUserByUsername(String username) {
        log.error("验证密码超时");
        return null;
    }
}
