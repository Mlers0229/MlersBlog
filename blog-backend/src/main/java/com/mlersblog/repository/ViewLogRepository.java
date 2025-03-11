package com.mlersblog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.mlersblog.model.ViewLog;

/**
 * 浏览记录存储库接口
 * 
 * 提供浏览记录相关的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {
    
    /**
     * 根据文章ID查询浏览记录
     * 
     * @param postId 文章ID
     * @return 浏览记录列表
     */
    List<ViewLog> findByPostId(Long postId);
    
    /**
     * 根据用户ID查询浏览记录
     * 
     * @param userId 用户ID
     * @return 浏览记录列表
     */
    List<ViewLog> findByUserId(Long userId);
    
    /**
     * 查询用户在指定时间段内的浏览记录
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 浏览记录列表
     */
    List<ViewLog> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询指定文章在指定时间段内的浏览记录
     * 
     * @param postId 文章ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 浏览记录列表
     */
    List<ViewLog> findByPostIdAndCreatedAtBetween(Long postId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计文章的浏览次数
     * 
     * @param postId 文章ID
     * @return 浏览次数
     */
    long countByPostId(Long postId);
    
    /**
     * 统计用户的浏览次数
     * 
     * @param userId 用户ID
     * @return 浏览次数
     */
    long countByUserId(Long userId);
    
    /**
     * 获取指定时间段内的总浏览量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 浏览量
     */
    long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询用户最近浏览的文章ID列表
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 文章ID列表
     */
    @Query("SELECT DISTINCT v.post.id FROM ViewLog v WHERE v.user.id = :userId ORDER BY v.createdAt DESC")
    List<Long> findRecentViewedPostIdsByUserId(@Param("userId") Long userId, Pageable pageable);
} 