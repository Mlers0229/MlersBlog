package com.mlersblog.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户设置实体类
 * 对应数据库中的user_setting表，存储用户个性化设置
 * 与User是一对一关系，共享主键
 */
@Entity
@Table(name = "user_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting {

    @Id
    private Long userId; // 用户ID，与User表共享主键

    // 一对一关系：通过共享主键的方式关联到User
    @OneToOne
    @MapsId // 表示使用user的id作为当前实体的主键
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user; // 关联的用户

    @Column(length = 20)
    private String theme = "light"; // 主题设置，默认为light

    @Column(name = "font_size")
    private Integer fontSize = 100; // 字体大小，默认为100%

    @Column(name = "email_notification")
    private Boolean emailNotification = true; // 是否启用邮件通知

    @Column(name = "site_notification")
    private Boolean siteNotification = true; // 是否启用站内通知

    @Column(name = "allow_comments")
    private Boolean allowComments = true; // 是否允许他人评论

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间

    /**
     * 用于创建新用户设置的构造函数
     * 
     * @param user 关联用户
     */
    public UserSetting(User user) {
        this.user = user;
    }
    
    /**
     * 切换主题
     * 
     * @param theme 主题名称
     */
    public void switchTheme(String theme) {
        this.theme = theme;
    }
    
    /**
     * 更改字体大小
     * 
     * @param fontSize 字体大小百分比
     */
    public void changeFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }
    
    /**
     * 切换邮件通知状态
     */
    public void toggleEmailNotification() {
        this.emailNotification = !this.emailNotification;
    }
    
    /**
     * 切换站内通知状态
     */
    public void toggleSiteNotification() {
        this.siteNotification = !this.siteNotification;
    }
    
    /**
     * 切换评论允许状态
     */
    public void toggleAllowComments() {
        this.allowComments = !this.allowComments;
    }
} 