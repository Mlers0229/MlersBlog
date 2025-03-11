package com.mlersblog.service.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mlersblog.model.Post;
import com.mlersblog.model.Post.PostStatus;
import com.mlersblog.model.Tag;

/**
 * 文章服务接口
 * 
 * 定义文章相关的业务逻辑方法
 */
public interface PostService {
    
    /**
     * 根据ID获取文章
     * 
     * @param id 文章ID
     * @return 文章对象
     * @throws ResourceNotFoundException 如果文章不存在
     */
    Post getPostById(Long id);
    
    /**
     * 创建新文章
     * 
     * @param post 文章对象
     * @param tagNames 标签名称列表
     * @param authorId 作者ID
     * @return 创建的文章
     */
    Post createPost(Post post, List<String> tagNames, Long authorId);
    
    /**
     * 更新文章
     * 
     * @param post 文章对象
     * @param tagNames 标签名称列表
     * @return 更新后的文章
     */
    Post updatePost(Post post, List<String> tagNames);
    
    /**
     * 删除文章
     * 
     * @param id 文章ID
     * @param currentUserId 当前用户ID
     * @return 是否删除成功
     */
    boolean deletePost(Long id, Long currentUserId);
    
    /**
     * 发布文章
     * 
     * @param id 文章ID
     * @param currentUserId 当前用户ID
     * @return 发布后的文章
     */
    Post publishPost(Long id, Long currentUserId);
    
    /**
     * 将文章设为草稿
     * 
     * @param id 文章ID
     * @param currentUserId 当前用户ID
     * @return 设置后的文章
     */
    Post saveAsDraft(Long id, Long currentUserId);
    
    /**
     * 获取文章的所有标签
     * 
     * @param postId 文章ID
     * @return 标签集合
     */
    Set<Tag> getPostTags(Long postId);
    
    /**
     * 增加文章浏览量
     * 
     * @param postId 文章ID
     * @param ip 访问者IP地址
     * @param userId 访问者ID（可能为null）
     */
    void incrementViewCount(Long postId, String ip, Long userId);
    
    /**
     * 获取用户的文章
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getUserPosts(Long userId, Pageable pageable);
    
    /**
     * 获取用户的特定状态的文章
     * 
     * @param userId 用户ID
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getUserPostsByStatus(Long userId, PostStatus status, Pageable pageable);
    
    /**
     * 获取已发布的文章
     * 
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getPublishedPosts(Pageable pageable);
    
    /**
     * 根据分类获取文章
     * 
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getPostsByCategory(Long categoryId, Pageable pageable);
    
    /**
     * 根据标签获取文章
     * 
     * @param tagId 标签ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getPostsByTag(Long tagId, Pageable pageable);
    
    /**
     * 搜索文章
     * 
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> searchPosts(String keyword, Pageable pageable);
    
    /**
     * 根据时间范围查询文章（用于归档）
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * 获取最近发布的文章
     * 
     * @param count 数量
     * @return 文章列表
     */
    List<Post> getRecentPosts(int count);
    
    /**
     * 获取热门文章
     * 
     * @param count 数量
     * @return 文章列表
     */
    List<Post> getPopularPosts(int count);
    
    /**
     * 获取推荐文章
     * 
     * @param count 数量
     * @return 文章列表
     */
    List<Post> getRecommendedPosts(int count);
    
    /**
     * 获取文章数量
     * 
     * @return 文章总数
     */
    long getPostCount();
    
    /**
     * 获取已发布文章数量
     * 
     * @return 已发布文章数
     */
    long getPublishedPostCount();
    
    /**
     * 获取用户文章统计信息
     * 
     * @param userId 用户ID
     * @return 包含文章数、浏览量、点赞数的数组
     */
    Object[] getUserPostStats(Long userId);
    
    /**
     * 获取所有已发布的文章
     * 
     * @return 文章列表
     */
    List<Post> getAllPublishedPosts();
} 