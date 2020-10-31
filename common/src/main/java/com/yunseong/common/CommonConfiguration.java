package com.yunseong.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;

@Configuration
public class CommonConfiguration {

    @Bean
    public AES256Util aes256Util() throws UnsupportedEncodingException {
        return new AES256Util();
    }
}
