package com.platform.recalldev.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Tag name is required")
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
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