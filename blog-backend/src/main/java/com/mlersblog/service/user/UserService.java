package com.mlersblog.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mlersblog.model.User;
import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.User.UserStatus;

/**
 * 用户服务接口
 * 
 * 定义用户相关的业务逻辑方法
 * 扩展Spring Security的UserDetailsService接口，用于认证
 */
public interface UserService extends UserDetailsService {
    
    /**
     * 根据用户ID获取用户
     * 
     * @param id 用户ID
     * @return 用户对象
     * @throws 如果未找到用户则抛出异常
     */
    User getUserById(Long id);
    
    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户对象
     * @throws 如果未找到用户则抛出异常
     */
    User getUserByUsername(String username);
    
    /**
     * 根据邮箱获取用户
     * 
     * @param email 电子邮箱
     * @return 用户对象
     * @throws 如果未找到用户则抛出异常
     */
    User getUserByEmail(String email);
    
    /**
     * 注册新用户
     * 
     * @param user 用户信息
     * @param rawPassword 原始密码(未加密)
     * @return 注册成功的用户
     */
    User registerUser(User user, String rawPassword);
    
    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新后的用户
     */
    User updateUser(User user);
    
    /**
     * 修改用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码（发送重置链接到用户邮箱）
     * 
     * @param email 用户邮箱
     * @return 是否发送成功
     */
    boolean resetPassword(String email);
    
    /**
     * 更新用户角色
     * 
     * @param userId 用户ID
     * @param role 新角色
     * @return 更新后的用户
     */
    User updateUserRole(Long userId, UserRole role);
    
    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 新状态
     * @return 更新后的用户
     */
    User updateUserStatus(Long userId, UserStatus status);
    
    /**
     * 检查用户名是否已存在
     * 
     * @param username 用户名
     * @return 如果已存在则返回true，否则返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否已存在
     * 
     * @param email 电子邮箱
     * @return 如果已存在则返回true，否则返回false
     */
    boolean existsByEmail(String email);
    
    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    List<User> findAllUsers();
    
    /**
     * 分页查询用户
     * 
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findAllUsers(Pageable pageable);
    
    /**
     * 根据角色查询用户
     * 
     * @param role 用户角色
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findUsersByRole(UserRole role, Pageable pageable);
    
    /**
     * 根据状态查询用户
     * 
     * @param status 用户状态
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findUsersByStatus(UserStatus status, Pageable pageable);
    
    /**
     * 搜索用户
     * 
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> searchUsers(String keyword, Pageable pageable);
    
    /**
     * 获取用户统计数据
     * 
     * @return 用户数量
     */
    long getUserCount();
    
    /**
     * 获取最近注册的用户
     * 
     * @param count 数量
     * @return 用户列表
     */
    List<User> getRecentUsers(int count);
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     */
    void deleteUser(Long userId);
} 