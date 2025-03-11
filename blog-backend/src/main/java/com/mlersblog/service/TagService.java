package com.mlersblog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mlersblog.model.Tag;

/**
 * 标签服务接口
 * 
 * 定义标签相关的业务逻辑方法
 */
public interface TagService {
    
    /**
     * 根据ID获取标签
     * 
     * @param id 标签ID
     * @return 标签对象
     * @throws ResourceNotFoundException 如果标签不存在
     */
    Tag getTagById(Long id);
    
    /**
     * 根据名称获取标签
     * 
     * @param name 标签名称
     * @return 标签对象
     * @throws ResourceNotFoundException 如果标签不存在
     */
    Tag getTagByName(String name);
    
    /**
     * 获取或创建标签
     * 如果标签不存在则创建，存在则返回已有标签
     * 
     * @param name 标签名称
     * @return 标签对象
     */
    Tag getOrCreateTag(String name);
    
    /**
     * 获取或创建多个标签
     * 
     * @param tagNames 标签名称列表
     * @return 标签对象列表
     */
    List<Tag> getOrCreateTags(List<String> tagNames);
    
    /**
     * 创建新标签
     * 
     * @param tag 标签对象
     * @return 创建的标签
     */
    Tag createTag(Tag tag);
    
    /**
     * 更新标签
     * 
     * @param tag 标签对象
     * @return 更新后的标签
     */
    Tag updateTag(Tag tag);
    
    /**
     * 删除标签
     * 
     * @param id 标签ID
     */
    void deleteTag(Long id);
    
    /**
     * 获取所有标签
     * 
     * @return 标签列表
     */
    List<Tag> getAllTags();
    
    /**
     * 分页获取所有标签
     * 
     * @param pageable 分页参数
     * @return 标签分页结果
     */
    Page<Tag> getAllTags(Pageable pageable);
    
    /**
     * 获取热门标签
     * 
     * @param limit 限制数量
     * @return 热门标签列表
     */
    List<Tag> getPopularTags(int limit);
    
    /**
     * 获取每个标签的文章数量
     * 
     * @return 标签ID-名称-文章数量的映射
     */
    List<Map<String, Object>> getTagsWithPostCount();
    
    /**
     * 检查标签名称是否已存在
     * 
     * @param name 标签名称
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByName(String name);
    
    /**
     * 根据关键字搜索标签
     * 
     * @param keyword 关键字
     * @return 标签列表
     */
    List<Tag> searchTags(String keyword);
} 