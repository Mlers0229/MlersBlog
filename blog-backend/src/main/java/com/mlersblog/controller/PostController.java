package com.mlersblog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlersblog.dto.PostDTO;
import com.mlersblog.model.Post;
import com.mlersblog.model.Post.PostStatus;
import com.mlersblog.model.Tag;
import com.mlersblog.model.User;
import com.mlersblog.service.post.PostService;
import com.mlersblog.service.user.UserService;
import com.mlersblog.util.PaginationUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 文章控制器
 * 
 * 处理文章相关的HTTP请求
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;

    /**
     * 获取文章列表 - 公开API，允许匿名访问
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Post> postPage = postService.getPublishedPosts(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("posts", convertToDTOList(postPage.getContent()));
        response.put("currentPage", postPage.getNumber());
        response.put("totalItems", postPage.getTotalElements());
        response.put("totalPages", postPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取公开文章列表的别名接口 - 兼容前端API调用
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */
    @GetMapping("/public")
    public ResponseEntity<?> getPublicPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return getAllPosts(page, size);
    }
    
    /**
     * 获取指定ID的文章 - 公开API，允许匿名访问
     * 
     * @param id 文章ID
     * @param request HTTP请求对象
     * @param authentication 认证对象
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(
            @PathVariable Long id,
            HttpServletRequest request,
            Authentication authentication) {
        
        Post post = postService.getPostById(id);
        
        // 增加浏览量
        String ip = request.getRemoteAddr();
        Long userId = null;
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername());
            userId = user.getId();
        }
        
        postService.incrementViewCount(id, ip, userId);
        
        return ResponseEntity.ok(convertToDTO(post));
    }
    
    /**
     * 创建文章
     * 
     * @param postDTO 文章数据
     * @param authentication 认证对象
     * @return 创建的文章
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostDTO postDTO,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setSummary(postDTO.getSummary());
        post.setCoverImage(postDTO.getCoverImage());
        post.setStatus(postDTO.getStatus() != null ? postDTO.getStatus() : PostStatus.DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        
        if (postDTO.getCategoryId() != null) {
            post.getCategory().setId(postDTO.getCategoryId());
        }
        
        Post createdPost = postService.createPost(post, postDTO.getTagNames(), userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdPost));
    }
    
    /**
     * 更新文章
     * 
     * @param id 文章ID
     * @param postDTO 文章数据
     * @param authentication 认证对象
     * @return 更新后的文章
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostDTO postDTO,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        Post existingPost = postService.getPostById(id);
        
        // 检查权限
        if (!existingPost.getAuthor().getId().equals(userId) && !user.getRole().equals(User.UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "您没有权限更新此文章"));
        }
        
        // 更新文章信息
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setSummary(postDTO.getSummary());
        existingPost.setCoverImage(postDTO.getCoverImage());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        if (postDTO.getCategoryId() != null) {
            existingPost.getCategory().setId(postDTO.getCategoryId());
        }
        
        Post updatedPost = postService.updatePost(existingPost, postDTO.getTagNames());
        
        return ResponseEntity.ok(convertToDTO(updatedPost));
    }
    
    /**
     * 删除文章
     * 
     * @param id 文章ID
     * @param authentication 认证对象
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        boolean result = postService.deletePost(id, userId);
        
        if (result) {
            return ResponseEntity.ok(Map.of("message", "文章删除成功"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "文章删除失败"));
        }
    }
    
    /**
     * 发布文章
     * 
     * @param id 文章ID
     * @param authentication 认证对象
     * @return 发布结果
     */
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> publishPost(
            @PathVariable Long id,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        Post publishedPost = postService.publishPost(id, userId);
        
        return ResponseEntity.ok(convertToDTO(publishedPost));
    }
    
    /**
     * 将文章设为草稿
     * 
     * @param id 文章ID
     * @param authentication 认证对象
     * @return 操作结果
     */
    @PutMapping("/{id}/draft")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> saveAsDraft(
            @PathVariable Long id,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        Post draftPost = postService.saveAsDraft(id, userId);
        
        return ResponseEntity.ok(convertToDTO(draftPost));
    }
    
    /**
     * 获取用户的文章
     * 
     * @param userId 用户ID
     * @param status 文章状态
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(required = false) PostStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage;
        
        if (status != null) {
            postPage = postService.getUserPostsByStatus(userId, status, pageable);
        } else {
            postPage = postService.getUserPosts(userId, pageable);
        }
        
        Map<String, Object> response = PaginationUtil.createResponse(
                convertToDTOList(postPage.getContent()),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取当前用户的文章
     * 
     * @param status 文章状态
     * @param page 页码
     * @param size 每页大小
     * @param authentication 认证对象
     * @return 文章列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getMyPosts(
            @RequestParam(required = false) PostStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Long userId = user.getId();
        
        return getUserPosts(userId, status, page, size);
    }
    
    /**
     * 根据分类获取文章 - 公开API，允许匿名访问
     * 
     * @param categoryId 分类ID
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getPostsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.getPostsByCategory(categoryId, pageable);
        
        Map<String, Object> response = PaginationUtil.createResponse(
                convertToDTOList(postPage.getContent()),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据标签获取文章 - 公开API，允许匿名访问
     * 
     * @param tagId 标签ID
     * @param page 页码
     * @param size 每页大小
     * @return 文章列表
     */
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<?> getPostsByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.getPostsByTag(tagId, pageable);
        
        Map<String, Object> response = PaginationUtil.createResponse(
                convertToDTOList(postPage.getContent()),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取文章归档数据 - 公开API，允许匿名访问
     * 
     * @return 归档数据
     */
    @GetMapping("/archive")
    public ResponseEntity<?> getArchiveData() {
        // 按年月分组获取文章
        List<Post> allPosts = postService.getAllPublishedPosts();
        
        // 按年月分组
        Map<Integer, Map<Integer, List<PostDTO>>> archiveData = new HashMap<>();
        
        for (Post post : allPosts) {
            if (post.getPublishedAt() != null) {
                int year = post.getPublishedAt().getYear();
                int month = post.getPublishedAt().getMonthValue();
                
                // 确保年份存在
                if (!archiveData.containsKey(year)) {
                    archiveData.put(year, new HashMap<>());
                }
                
                // 确保月份存在
                Map<Integer, List<PostDTO>> yearData = archiveData.get(year);
                if (!yearData.containsKey(month)) {
                    yearData.put(month, new ArrayList<>());
                }
                
                // 添加文章
                yearData.get(month).add(convertToDTO(post));
            }
        }
        
        return ResponseEntity.ok(archiveData);
    }
    
    /**
     * 搜索文章 - 公开API，允许匿名访问
     * 
     * @param keyword 关键字
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.searchPosts(keyword, pageable);
        
        Map<String, Object> response = PaginationUtil.createResponse(
                convertToDTOList(postPage.getContent()),
                postPage.getNumber(),
                postPage.getTotalElements(),
                postPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取最近发布的文章 - 公开API，允许匿名访问
     * 
     * @param count 数量
     * @return 文章列表
     */
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentPosts(
            @RequestParam(defaultValue = "5") int count) {
        
        List<Post> posts = postService.getRecentPosts(count);
        return ResponseEntity.ok(convertToDTOList(posts));
    }
    
    /**
     * 获取热门文章 - 公开API，允许匿名访问
     * 
     * @param count 数量
     * @return 文章列表
     */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularPosts(
            @RequestParam(defaultValue = "5") int count) {
        
        List<Post> posts = postService.getPopularPosts(count);
        return ResponseEntity.ok(convertToDTOList(posts));
    }
    
    /**
     * 获取推荐文章 - 公开API，允许匿名访问
     * 
     * @param count 数量
     * @return 文章列表
     */
    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedPosts(
            @RequestParam(defaultValue = "5") int count) {
        
        List<Post> posts = postService.getRecommendedPosts(count);
        return ResponseEntity.ok(convertToDTOList(posts));
    }
    
    /**
     * 获取文章标签
     * 
     * @param id 文章ID
     * @return 标签列表
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<?> getPostTags(@PathVariable Long id) {
        Set<Tag> tags = postService.getPostTags(id);
        return ResponseEntity.ok(tags);
    }
    
    /**
     * 将Post实体转换为DTO
     * 
     * @param post Post实体
     * @return PostDTO
     */
    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setSummary(post.getSummary());
        dto.setCoverImage(post.getCoverImage());
        dto.setStatus(post.getStatus());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setFavoriteCount(post.getFavoriteCount());
        
        // 设置作者信息
        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorName(post.getAuthor().getUsername());
        }
        
        // 设置分类信息
        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }
        
        // 设置标签信息
        Set<Tag> tags = postService.getPostTags(post.getId());
        if (tags != null && !tags.isEmpty()) {
            dto.setTagNames(tags.stream().map(Tag::getName).collect(Collectors.toList()));
        }
        
        // 设置时间信息
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setPublishedAt(post.getPublishedAt());
        
        return dto;
    }
    
    /**
     * 将Post实体列表转换为DTO列表
     * 
     * @param posts Post实体列表
     * @return PostDTO列表
     */
    private List<PostDTO> convertToDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
} 