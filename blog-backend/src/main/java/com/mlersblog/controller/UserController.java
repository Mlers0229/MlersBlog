package com.mlersblog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlersblog.dto.UserDTO;
import com.mlersblog.dto.request.ChangePasswordRequest;
import com.mlersblog.dto.request.RegisterRequest;
import com.mlersblog.dto.request.UpdateUserRequest;
import com.mlersblog.model.User;
import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.User.UserStatus;
import com.mlersblog.service.user.UserService;

import jakarta.validation.Valid;

/**
 * 用户控制器
 * 
 * 处理用户相关的HTTP请求
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     * 
     * @param registerRequest 注册请求数据
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setNickname(registerRequest.getNickname());
        
        User registeredUser = userService.registerUser(user, registerRequest.getPassword());
        return new ResponseEntity<>(convertToDTO(registeredUser), HttpStatus.CREATED);
    }
    
    /**
     * 获取当前登录用户信息
     * 
     * @param userDetails 当前认证用户
     * @return 用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(convertToDTO(user));
    }
    
    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }
    
    /**
     * 修改密码
     * 
     * @param request 修改密码请求
     * @param userDetails 当前认证用户
     * @return 操作结果
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        boolean success = userService.changePassword(user.getId(), request.getOldPassword(), request.getNewPassword());
        
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "密码修改成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "密码修改失败，旧密码不正确");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param updateRequest 更新请求数据
     * @param userDetails 当前认证用户
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserRequest updateRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // 检查权限：只允许更新自己的信息或管理员权限
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        User userToUpdate = userService.getUserById(id);
        userToUpdate.setNickname(updateRequest.getNickname());
        userToUpdate.setAvatar(updateRequest.getAvatar());
        userToUpdate.setBio(updateRequest.getBio());
        
        User updatedUser = userService.updateUser(userToUpdate);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }
    
    /**
     * 获取所有用户（管理员权限）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向
     * @return 用户分页结果
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<User> usersPage = userService.findAllUsers(pageable);
        
        Page<UserDTO> userDTOPage = usersPage.map(this::convertToDTO);
        return ResponseEntity.ok(userDTOPage);
    }
    
    /**
     * 更新用户角色（管理员权限）
     * 
     * @param id 用户ID
     * @param role 新角色
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long id,
            @RequestParam UserRole role) {
        
        User updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }
    
    /**
     * 更新用户状态（管理员权限）
     * 
     * @param id 用户ID
     * @param status 新状态
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status) {
        
        User updatedUser = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }
    
    /**
     * 删除用户（管理员权限）
     * 
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 搜索用户（管理员权限）
     * 
     * @param keyword 关键字
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.searchUsers(keyword, pageable);
        
        Page<UserDTO> userDTOPage = usersPage.map(this::convertToDTO);
        return ResponseEntity.ok(userDTOPage);
    }
    
    /**
     * 获取最近注册用户
     * 
     * @param count 数量
     * @return 用户列表
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getRecentUsers(@RequestParam(defaultValue = "10") int count) {
        List<User> users = userService.getRecentUsers(count);
        List<UserDTO> userDTOs = users.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(userDTOs);
    }
    
    /**
     * 获取用户总数
     * 
     * @return 用户总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        long count = userService.getUserCount();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
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