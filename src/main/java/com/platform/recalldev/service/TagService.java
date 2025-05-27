package com.platform.recalldev.service;

import com.platform.recalldev.entity.Tag;
import com.platform.recalldev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService {
    
    private final TagRepository tagRepository;
    
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    
    public List<Tag> getAllTagsWithQuestions() {
        return tagRepository.findAllWithQuestions();
    }
    
    public Optional<Tag> getTagById(Integer id) {
        return tagRepository.findById(id);
    }
    
    public Tag getTagByIdWithQuestions(Integer id) {
        return tagRepository.findByIdWithQuestions(id);
    }
    
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }
    
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
    
    public Tag createTag(String name) {
        // Check if tag already exists
        Optional<Tag> existingTag = tagRepository.findByName(name);
        if (existingTag.isPresent()) {
            throw new RuntimeException("Tag with name '" + name + "' already exists");
        }
        
        Tag tag = Tag.builder().name(name).build();
        return tagRepository.save(tag);
    }
    
    public Tag updateTag(Integer id, String name) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
        
        // Check if another tag with the same name exists
        Optional<Tag> existingTag = tagRepository.findByName(name);
        if (existingTag.isPresent() && !existingTag.get().getId().equals(id)) {
            throw new RuntimeException("Tag with name '" + name + "' already exists");
        }
        
        tag.setName(name);
        return tagRepository.save(tag);
    }
    
    public void deleteTag(Integer id) {
        tagRepository.deleteById(id);
    }
    
    public List<Tag> searchTags(String keyword) {
        return tagRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Tag> getTagsWithQuestions() {
        return tagRepository.findTagsWithQuestions();
    }
    
    public List<Tag> getTagsWithoutQuestions() {
        return tagRepository.findTagsWithoutQuestions();
    }
    
    public Tag getOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
    }
} 