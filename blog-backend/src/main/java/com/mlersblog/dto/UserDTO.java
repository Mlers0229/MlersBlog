package com.mlersblog.dto;

import java.time.LocalDateTime;

import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.User.UserStatus;

import lombok.Data;

/**
 * 用户数据传输对象
 * 
 * 用于API响应中返回用户信息，不包含敏感数据如密码
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private String bio;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 