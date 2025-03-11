package com.mlersblog.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * 对应数据库中的user表，存储用户基本信息
 * 包括用户名、密码、邮箱、昵称、头像等
 */
@Entity
@Table(name = "user") // 指定表名
@Data // Lombok注解，自动生成getter、setter、equals、hashCode等方法
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参数构造函数
public class User {

    @Id // 主键标识
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略为自增
    private Long id;

    @Column(unique = true, nullable = false, length = 50) // 唯一约束，非空约束，长度限制
    private String username; // 用户名

    @Column(nullable = false)
    private String password; // 密码（加密存储）

    @Column(unique = true, nullable = false, length = 100)
    private String email; // 电子邮箱

    @Column(nullable = false, length = 50)
    private String nickname; // 用户昵称

    @Column
    private String avatar; // 头像URL

    @Column(columnDefinition = "TEXT")
    private String bio; // 个人简介

    @Enumerated(EnumType.STRING) // 枚举类型映射为字符串
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 用户角色，默认为普通用户

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // 用户状态，默认为活跃

    @CreationTimestamp // 自动设置创建时间
    private LocalDateTime createdAt; // 创建时间

    @UpdateTimestamp // 自动更新修改时间
    private LocalDateTime updatedAt; // 更新时间

    // 一对多关系：一个用户可以发布多篇文章
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    // 一对多关系：一个用户可以发表多条评论
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    // 一对一关系：一个用户对应一个用户设置
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSetting userSetting;

    /**
     * 用户角色枚举
     */
    public enum UserRole {
        USER, // 普通用户
        ADMIN // 管理员
    }

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        ACTIVE, // 活跃状态
        LOCKED, // 锁定状态
        INACTIVE // 非活跃状态
    }
} 