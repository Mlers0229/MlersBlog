package com.mlersblog.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlersblog.exception.ResourceNotFoundException;
import com.mlersblog.exception.UserAlreadyExistsException;
import com.mlersblog.model.User;
import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.User.UserStatus;
import com.mlersblog.model.UserSetting;
import com.mlersblog.repository.user.UserRepository;

/**
 * 用户服务实现类
 * 
 * 实现UserService接口定义的所有方法
 * 处理用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 实现UserDetailsService接口的方法，用于Spring Security认证
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在: " + username));
        
        // 检查用户状态
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("用户已锁定或未激活: " + username);
        }
        
        // 创建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
        // 返回UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID为 " + id + " 的用户"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("找不到用户名为 " + username + " 的用户"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("找不到邮箱为 " + email + " 的用户"));
    }

    @Override
    @Transactional
    public User registerUser(User user, String rawPassword) {
        // 检查用户名和邮箱是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("用户名已存在: " + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("邮箱已存在: " + user.getEmail());
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(rawPassword));
        
        // 设置默认值
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 创建用户设置
        UserSetting userSetting = new UserSetting(savedUser);
        savedUser.setUserSetting(userSetting);
        
        // 返回保存的用户
        return userRepository.save(savedUser);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // 获取数据库中的用户
        User existingUser = getUserById(user.getId());
        
        // 更新字段
        existingUser.setNickname(user.getNickname());
        existingUser.setAvatar(user.getAvatar());
        existingUser.setBio(user.getBio());
        
        // 保存并返回
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 获取用户
        User user = getUserById(userId);
        
        // 检查旧密码是否正确
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        return true;
    }

    @Override
    public boolean resetPassword(String email) {
        // 此处应该实现发送重置密码链接到邮箱的逻辑
        // 为简化起见，我们只检查邮箱是否存在
        try {
            getUserByEmail(email);
            // TODO: 发送重置密码邮件
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public User updateUserRole(Long userId, UserRole role) {
        User user = getUserById(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserStatus(Long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Override
    public Page<User> findUsersByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.search(keyword, pageable);
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

    @Override
    public List<User> getRecentUsers(int count) {
        return userRepository.findRecentUsers(PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // 实际项目中可能不会真正删除用户，而是将其状态设置为INACTIVE
        User user = getUserById(userId);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        
        // 如果确实需要删除
        // userRepository.deleteById(userId);
    }
} 