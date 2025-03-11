package com.mlersblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置
 * 
 * 将PasswordEncoder单独提取到一个配置类中，以避免循环依赖问题
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * 创建密码编码器Bean
     * 
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 