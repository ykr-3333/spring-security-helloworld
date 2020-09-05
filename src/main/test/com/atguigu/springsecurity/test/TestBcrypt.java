package com.atguigu.springsecurity.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder bp = new BCryptPasswordEncoder();
        //$2a$10$WzJbUEPObNpUXz99YQlHBei7MPo1WrF1XnvHd9lnWdRevMpe2mQL.
        //$2a$10$3w0AxEz1.dL98fPggwhJwOylQ1f1SYz7yxBis/uemfLkEmgMu/gNK
        //$2a$10$Cm8.CP2zWe8BzuG/qfNxiOVnEFHcEuopHADBfnAhiZKlQc2yIZ9su
        String encode = bp.encode("123456"); //60长度
        System.out.println(encode);

        System.out.println("$2a$10$3w0AxEz1.dL98fPggwhJwOylQ1f1SYz7yxBis/uemfLkEmgMu/gNK".length());
    }
}
