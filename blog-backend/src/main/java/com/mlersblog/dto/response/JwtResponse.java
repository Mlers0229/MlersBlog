package com.mlersblog.dto.response;

import com.mlersblog.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JWT响应
 * 
 * 包含认证成功后的JWT令牌和用户信息
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private UserDTO user;
} 