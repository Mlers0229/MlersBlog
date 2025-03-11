package com.mlersblog.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mlersblog.model.User;
import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.User.UserStatus;

/**
 * 用户存储库接口
 * 
 * 使用Spring Data JPA提供用户相关的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 包含用户的Optional对象
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     * 
     * @param email 电子邮箱
     * @return 包含用户的Optional对象
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 检查用户名是否已存在
     * 
     * @param username 用户名
     * @return 如果用户名已存在则返回true，否则返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否已存在
     * 
     * @param email 电子邮箱
     * @return 如果邮箱已存在则返回true，否则返回false
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据角色查找用户列表
     * 
     * @param role 用户角色
     * @return 用户列表
     */
    List<User> findByRole(UserRole role);
    
    /**
     * 根据状态查找用户列表
     * 
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> findByStatus(UserStatus status);
    
    /**
     * 分页查询用户
     * 
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Override
    Page<User> findAll(Pageable pageable);
    
    /**
     * 根据角色分页查询用户
     * 
     * @param role 用户角色
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    /**
     * 根据状态分页查询用户
     * 
     * @param status 用户状态
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    
    /**
     * 根据关键字搜索用户（用户名、昵称或邮箱包含关键字）
     * 
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 用户分页结果
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.nickname LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查询最近注册的用户
     * 
     * @param limit 限制数量
     * @return 用户列表
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers(Pageable pageable);
    
    /**
     * 根据IDs批量查询用户
     * 
     * @param ids 用户ID列表
     * @return 用户列表
     */
    List<User> findByIdIn(List<Long> ids);
} 