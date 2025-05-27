package com.platform.recalldev.repository;

import com.platform.recalldev.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    List<Question> findByDifficulty(Question.DifficultyLevel difficulty);
    
    List<Question> findByQuestionTextContainingIgnoreCase(String keyword);
    
    List<Question> findByQuestionAnswerContainingIgnoreCase(String keyword);
    
    @Query("SELECT q FROM Question q WHERE q.questionText LIKE %:keyword% OR q.questionAnswer LIKE %:keyword%")
    List<Question> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.name = :tagName")
    List<Question> findByTagName(@Param("tagName") String tagName);
    
    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.name IN :tagNames")
    List<Question> findByTagNames(@Param("tagNames") List<String> tagNames);
    
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.tags WHERE q.id = :id")
    Question findByIdWithTags(@Param("id") Integer id);
    
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.tags")
    List<Question> findAllWithTags();
} 