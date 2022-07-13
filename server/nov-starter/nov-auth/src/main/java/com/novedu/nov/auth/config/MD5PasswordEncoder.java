package com.novedu.nov.auth.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5PasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence charSequence) {
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes(StandardCharsets.UTF_8)).equals(encodedPassword);
    }
}
