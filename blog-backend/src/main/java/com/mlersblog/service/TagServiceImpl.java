package com.mlersblog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlersblog.exception.ResourceNotFoundException;
import com.mlersblog.model.Tag;
import com.mlersblog.repository.TagRepository;

/**
 * 标签服务实现类
 * 
 * 实现TagService接口中定义的所有方法，
 * 处理标签相关业务逻辑
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在，ID: " + id));
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在，名称: " + name));
    }

    @Override
    @Transactional
    public Tag getOrCreateTag(String name) {
        // 先尝试查找标签
        Optional<Tag> existingTag = tagRepository.findByName(name);
        
        // 如果存在则返回，否则创建新标签
        if (existingTag.isPresent()) {
            return existingTag.get();
        } else {
            Tag newTag = new Tag();
            newTag.setName(name);
            return tagRepository.save(newTag);
        }
    }

    @Override
    @Transactional
    public List<Tag> getOrCreateTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 过滤空标签并去重
        List<String> uniqueNames = tagNames.stream()
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        
        // 获取或创建每个标签
        return uniqueNames.stream()
                .map(this::getOrCreateTag)
                .collect(Collectors.toList());
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Tag tag) {
        // 检查标签是否存在
        Tag existingTag = getTagById(tag.getId());
        
        // 更新标签信息
        existingTag.setName(tag.getName());
        
        return tagRepository.save(existingTag);
    }

    @Override
    public void deleteTag(Long id) {
        // 检查标签是否存在
        getTagById(id);
        
        // 删除标签
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Page<Tag> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> getPopularTags(int limit) {
        return tagRepository.findPopularTags(limit);
    }

    @Override
    public List<Map<String, Object>> getTagsWithPostCount() {
        List<Object[]> results = tagRepository.findTagsWithPostCount();
        List<Map<String, Object>> tagStats = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("id", result[0]);
            tagMap.put("name", result[1]);
            tagMap.put("postCount", result[2]);
            tagStats.add(tagMap);
        }
        
        return tagStats;
    }

    @Override
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }

    @Override
    public List<Tag> searchTags(String keyword) {
        return tagRepository.search(keyword);
    }
} 