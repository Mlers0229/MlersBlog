package com.mlersblog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlersblog.dto.UserDTO;
import com.mlersblog.dto.request.LoginRequest;
import com.mlersblog.dto.response.JwtResponse;
import com.mlersblog.model.User;
import com.mlersblog.security.jwt.JwtUtils;
import com.mlersblog.service.user.UserService;

import jakarta.validation.Valid;

/**
 * 认证控制器
 * 
 * 处理用户登录、注销、刷新令牌等认证相关请求
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求
     * @return JWT令牌和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 验证用户名密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), 
                            loginRequest.getPassword()));

            // 设置认证信息到上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成JWT令牌
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            
            // 获取用户信息
            User user = userService.getUserByUsername(userDetails.getUsername());
            UserDTO userDTO = convertToDTO(user);
            
            // 构建响应
            return ResponseEntity.ok(new JwtResponse(jwt, userDTO));
            
        } catch (BadCredentialsException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * 请求重置密码
     * 
     * @param email 用户邮箱
     * @return 操作结果
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String email = resetRequest.get("email");
        boolean success = userService.resetPassword(email);
        
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "密码重置链接已发送到您的邮箱");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "邮箱不存在或其他错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 转换User对象为UserDTO
     * 
     * @param user 用户对象
     * @return 用户DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
} 