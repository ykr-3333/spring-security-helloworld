package com.atguigu.security.component;

import com.atguigu.security.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MD5PasswordEncoder implements PasswordEncoder {

    //将原文加密成密文
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Util.digest(charSequence.toString());
    }

    //验证密码是否正确
    //rawPassword原文
    //encodedPassword 密文
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return MD5Util.digest(rawPassword.toString()).equals(encodedPassword);
    }
}
