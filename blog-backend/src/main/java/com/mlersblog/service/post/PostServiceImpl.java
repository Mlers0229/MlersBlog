package com.mlersblog.service.post;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlersblog.exception.ResourceNotFoundException;
import com.mlersblog.exception.UnauthorizedException;
import com.mlersblog.model.Post;
import com.mlersblog.model.Post.PostStatus;
import com.mlersblog.model.Tag;
import com.mlersblog.model.User;
import com.mlersblog.model.User.UserRole;
import com.mlersblog.model.ViewLog;
import com.mlersblog.repository.TagRepository;
import com.mlersblog.repository.ViewLogRepository;
import com.mlersblog.repository.post.PostRepository;
import com.mlersblog.service.user.UserService;

/**
 * 文章服务实现类
 * 
 * 实现PostService接口定义的所有方法
 * 处理文章相关的业务逻辑
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ViewLogRepository viewLogRepository;
    
    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("找不到ID为 " + id + " 的文章"));
    }

    @Override
    @Transactional
    public Post createPost(Post post, List<String> tagNames, Long authorId) {
        // 获取作者信息
        User author = userService.getUserById(authorId);
        post.setAuthor(author);
        
        // 设置默认值
        if (post.getStatus() == null) {
            post.setStatus(PostStatus.DRAFT);
        }
        
        // 如果是发布状态，设置发布时间
        if (post.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
        
        // 保存文章
        Post savedPost = postRepository.save(post);
        
        // 处理标签
        if (tagNames != null && !tagNames.isEmpty()) {
            processPostTags(savedPost, tagNames);
        }
        
        return savedPost;
    }

    @Override
    @Transactional
    public Post updatePost(Post post, List<String> tagNames) {
        // 获取数据库中的文章
        Post existingPost = getPostById(post.getId());
        
        // 更新基本字段
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setSummary(post.getSummary());
        existingPost.setCoverImage(post.getCoverImage());
        existingPost.setStatus(post.getStatus());
        
        // 如果更新为发布状态且原来不是发布状态
        if (post.getStatus() == PostStatus.PUBLISHED && existingPost.getPublishedAt() == null) {
            existingPost.setPublishedAt(LocalDateTime.now());
        }
        
        // 更新分类
        if (post.getCategory() != null) {
            existingPost.setCategory(post.getCategory());
        }
        
        // 处理标签
        if (tagNames != null) {
            processPostTags(existingPost, tagNames);
        }
        
        // 保存更新
        return postRepository.save(existingPost);
    }

    /**
     * 处理文章标签
     * 
     * @param post 文章对象
     * @param tagNames 标签名称列表
     */
    private void processPostTags(Post post, List<String> tagNames) {
        // 清除现有标签
        post.getTags().clear();
        
        // 处理每个标签
        for (String tagName : tagNames) {
            // 查找或创建标签
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            
            // 添加标签到文章
            post.addTag(tag);
        }
    }

    @Override
    @Transactional
    public boolean deletePost(Long id, Long currentUserId) {
        Post post = getPostById(id);
        User currentUser = userService.getUserById(currentUserId);
        
        // 检查权限：只有作者或管理员可以删除文章
        if (!post.getAuthor().getId().equals(currentUserId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("您没有权限删除此文章");
        }
        
        postRepository.delete(post);
        return true;
    }

    @Override
    @Transactional
    public Post publishPost(Long id, Long currentUserId) {
        Post post = getPostById(id);
        
        // 检查权限
        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedException("您没有权限发布此文章");
        }
        
        // 发布文章
        post.setStatus(PostStatus.PUBLISHED);
        post.setPublishedAt(LocalDateTime.now());
        
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post saveAsDraft(Long id, Long currentUserId) {
        Post post = getPostById(id);
        
        // 检查权限
        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedException("您没有权限修改此文章");
        }
        
        // 设为草稿
        post.setStatus(PostStatus.DRAFT);
        
        return postRepository.save(post);
    }

    @Override
    public Set<Tag> getPostTags(Long postId) {
        Post post = getPostById(postId);
        return post.getTags();
    }

    @Override
    @Transactional
    public void incrementViewCount(Long postId, String ip, Long userId) {
        Post post = getPostById(postId);
        
        // 更新文章浏览量
        postRepository.incrementViewCount(postId);
        
        // 记录浏览日志
        ViewLog viewLog = new ViewLog();
        viewLog.setPost(post);
        viewLog.setIp(ip);
        
        if (userId != null) {
            User user = userService.getUserById(userId);
            viewLog.setUser(user);
        }
        
        viewLogRepository.save(viewLog);
    }

    @Override
    public Page<Post> getUserPosts(Long userId, Pageable pageable) {
        return postRepository.findByAuthorId(userId, pageable);
    }

    @Override
    public Page<Post> getUserPostsByStatus(Long userId, PostStatus status, Pageable pageable) {
        // 创建一个带排序的Pageable对象
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // 查询指定状态的文章
        return postRepository.findByAuthorIdAndStatus(userId, status, sortedPageable);
    }

    @Override
    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByPublishedAtDesc(PostStatus.PUBLISHED, pageable);
    }

    @Override
    public Page<Post> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryIdAndStatus(categoryId, PostStatus.PUBLISHED, pageable);
    }

    @Override
    public Page<Post> getPostsByTag(Long tagId, Pageable pageable) {
        return postRepository.findByTagId(tagId, pageable);
    }

    @Override
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.search(keyword, pageable);
    }

    @Override
    public Page<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postRepository.findByPublishedAtBetween(startDate, endDate, pageable);
    }

    @Override
    public List<Post> getRecentPosts(int count) {
        return postRepository.findRecentPosts(PageRequest.of(0, count));
    }

    @Override
    public List<Post> getPopularPosts(int count) {
        return postRepository.findPopularPosts(PageRequest.of(0, count));
    }

    @Override
    public List<Post> getRecommendedPosts(int count) {
        return postRepository.findRecommendedPosts(PageRequest.of(0, count));
    }

    @Override
    public long getPostCount() {
        return postRepository.count();
    }

    @Override
    public long getPublishedPostCount() {
        return postRepository.findByStatus(PostStatus.PUBLISHED).size();
    }

    @Override
    public Object[] getUserPostStats(Long userId) {
        return postRepository.getAuthorStats(userId);
    }

    @Override
    public List<Post> getAllPublishedPosts() {
        return postRepository.findByStatus(PostStatus.PUBLISHED);
    }
} 