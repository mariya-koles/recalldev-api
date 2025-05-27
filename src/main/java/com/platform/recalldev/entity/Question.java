package com.platform.recalldev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Question text is required")
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @NotBlank(message = "Question answer is required")
    @Column(name = "question_answer", nullable = false, columnDefinition = "TEXT")
    private String questionAnswer;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "difficulty_level")
    @Builder.Default
    private DifficultyLevel difficulty = DifficultyLevel.MEDIUM;
    
    @ManyToMany(mappedBy = "questions", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
    
    // Helper methods
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getQuestions().add(this);
    }
    
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getQuestions().remove(this);
    }
} 