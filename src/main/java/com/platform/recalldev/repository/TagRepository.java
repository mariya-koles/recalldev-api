package com.platform.recalldev.repository;

import com.platform.recalldev.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    
    Optional<Tag> findByName(String name);
    
    List<Tag> findByNameContainingIgnoreCase(String keyword);
    
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.questions WHERE t.id = :id")
    Tag findByIdWithQuestions(@Param("id") Integer id);
    
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.questions")
    List<Tag> findAllWithQuestions();
    
    @Query("SELECT t FROM Tag t WHERE SIZE(t.questions) > 0")
    List<Tag> findTagsWithQuestions();
    
    @Query("SELECT t FROM Tag t WHERE SIZE(t.questions) = 0")
    List<Tag> findTagsWithoutQuestions();
} 