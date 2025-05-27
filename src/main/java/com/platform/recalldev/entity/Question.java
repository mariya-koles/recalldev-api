package com.platform.recalldev.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
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