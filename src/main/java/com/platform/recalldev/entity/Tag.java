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
@Table(name = "tags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Tag name is required")
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "question_tags",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @Builder.Default
    private Set<Question> questions = new HashSet<>();
    
    // Helper methods
    public void addQuestion(Question question) {
        this.questions.add(question);
        question.getTags().add(this);
    }
    
    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.getTags().remove(this);
    }
} 