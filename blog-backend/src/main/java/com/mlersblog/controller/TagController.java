package com.mlersblog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlersblog.dto.TagDTO;
import com.mlersblog.model.Tag;
import com.mlersblog.service.TagService;
import com.mlersblog.util.PaginationUtil;

import jakarta.validation.Valid;

/**
 * 标签控制器
 * 
 * 处理标签相关的HTTP请求
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取所有标签
     * 
     * @param includePostCount 是否包含文章数量
     * @return 标签列表
     */
    @GetMapping
    public ResponseEntity<?> getAllTags(@RequestParam(defaultValue = "false") boolean includePostCount) {
        if (includePostCount) {
            List<Map<String, Object>> tagStats = tagService.getTagsWithPostCount();
            return ResponseEntity.ok(tagStats);
        } else {
            List<Tag> tags = tagService.getAllTags();
            List<TagDTO> tagDTOs = tags.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(tagDTOs);
        }
    }
    
    /**
     * 获取热门标签
     * 
     * @param count 数量
     * @return 标签列表
     */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularTags(@RequestParam(defaultValue = "10") int count) {
        List<Tag> tags = tagService.getPopularTags(count);
        List<TagDTO> tagDTOs = tags.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tagDTOs);
    }
    
    /**
     * 获取标签详情
     * 
     * @param id 标签ID
     * @return 标签详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTagById(id);
        return ResponseEntity.ok(convertToDTO(tag));
    }
    
    /**
     * 按名称查询标签
     * 
     * @param name 标签名称
     * @return 标签详情
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getTagByName(@PathVariable String name) {
        Tag tag = tagService.getTagByName(name);
        return ResponseEntity.ok(convertToDTO(tag));
    }
    
    /**
     * 创建标签
     * 
     * @param tagDTO 标签数据
     * @return 创建的标签
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> createTag(@Valid @RequestBody TagDTO tagDTO) {
        // 检查标签名是否已存在
        if (tagService.existsByName(tagDTO.getName())) {
            return ResponseEntity.badRequest().body(Map.of("message", "标签名已存在"));
        }
        
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        
        Tag createdTag = tagService.createTag(tag);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdTag));
    }
    
    /**
     * 更新标签
     * 
     * @param id 标签ID
     * @param tagDTO 标签数据
     * @return 更新后的标签
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagDTO tagDTO) {
        
        // 检查标签是否存在
        Tag existingTag = tagService.getTagById(id);
        
        // 如果名称已更改，检查新名称是否已存在
        if (!existingTag.getName().equals(tagDTO.getName()) && tagService.existsByName(tagDTO.getName())) {
            return ResponseEntity.badRequest().body(Map.of("message", "标签名已存在"));
        }
        
        existingTag.setName(tagDTO.getName());
        
        Tag updatedTag = tagService.updateTag(existingTag);
        
        return ResponseEntity.ok(convertToDTO(updatedTag));
    }
    
    /**
     * 删除标签
     * 
     * @param id 标签ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(Map.of("message", "标签删除成功"));
    }
    
    /**
     * 搜索标签
     * 
     * @param keyword 关键字
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTags(@RequestParam String keyword) {
        List<Tag> tags = tagService.searchTags(keyword);
        List<TagDTO> tagDTOs = tags.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tagDTOs);
    }
    
    /**
     * 将Tag实体转换为DTO
     * 
     * @param tag Tag实体
     * @return TagDTO
     */
    private TagDTO convertToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setCreatedAt(tag.getCreatedAt());
        return dto;
    }
} 