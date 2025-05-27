package com.platform.recalldev.controller;

import com.platform.recalldev.entity.Question;
import com.platform.recalldev.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
@Tag(name = "Questions", description = "API for managing programming interview questions")
public class QuestionController {
    
    private final QuestionService questionService;
    
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    
    @GetMapping
    @Operation(summary = "Get all questions", description = "Retrieve all questions with optional tag inclusion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved questions",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)))
    })
    public ResponseEntity<List<Question>> getAllQuestions(
            @Parameter(description = "Include tags in the response") 
            @RequestParam(required = false) boolean includeTags) {
        List<Question> questions = includeTags ? 
                questionService.getAllQuestionsWithTags() : 
                questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID", description = "Retrieve a specific question by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Question> getQuestionById(
            @Parameter(description = "Question ID") @PathVariable Integer id,
            @Parameter(description = "Include tags in the response") 
            @RequestParam(required = false) boolean includeTags) {
        if (includeTags) {
            Question question = questionService.getQuestionByIdWithTags(id);
            return question != null ? ResponseEntity.ok(question) : ResponseEntity.notFound().build();
        } else {
            Optional<Question> question = questionService.getQuestionById(id);
            return question.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
    }
    
    @PostMapping
    @Operation(summary = "Create a new question", description = "Create a new programming interview question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Question> createQuestion(
            @Parameter(description = "Question data") @Valid @RequestBody QuestionRequest request) {
        Question question = questionService.createQuestion(
                request.getQuestionText(),
                request.getQuestionAnswer(),
                request.getDifficulty()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a question", description = "Update an existing question by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Question> updateQuestion(
            @Parameter(description = "Question ID") @PathVariable Integer id,
            @Parameter(description = "Updated question data") @Valid @RequestBody QuestionRequest request) {
        try {
            Question question = questionService.updateQuestion(
                    id,
                    request.getQuestionText(),
                    request.getQuestionAnswer(),
                    request.getDifficulty()
            );
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question", description = "Delete a question by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(description = "Question ID") @PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "Get questions by difficulty", description = "Retrieve questions filtered by difficulty level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)))
    })
    public ResponseEntity<List<Question>> getQuestionsByDifficulty(
            @Parameter(description = "Difficulty level (EASY, MEDIUM, HARD)") 
            @PathVariable Question.DifficultyLevel difficulty) {
        List<Question> questions = questionService.getQuestionsByDifficulty(difficulty);
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search questions", description = "Search questions by keyword in text or answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)))
    })
    public ResponseEntity<List<Question>> searchQuestions(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<Question> questions = questionService.searchQuestions(keyword);
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("/tag/{tagName}")
    @Operation(summary = "Get questions by tag", description = "Retrieve questions that have a specific tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)))
    })
    public ResponseEntity<List<Question>> getQuestionsByTag(
            @Parameter(description = "Tag name") @PathVariable String tagName) {
        List<Question> questions = questionService.getQuestionsByTag(tagName);
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("/tags")
    @Operation(summary = "Get questions by multiple tags", description = "Retrieve questions that have any of the specified tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)))
    })
    public ResponseEntity<List<Question>> getQuestionsByTags(
            @Parameter(description = "List of tag names") @RequestParam List<String> tagNames) {
        List<Question> questions = questionService.getQuestionsByTags(tagNames);
        return ResponseEntity.ok(questions);
    }
    
    @PostMapping("/{id}/tags/{tagName}")
    @Operation(summary = "Add tag to question", description = "Add a tag to an existing question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Question> addTagToQuestion(
            @Parameter(description = "Question ID") @PathVariable Integer id,
            @Parameter(description = "Tag name") @PathVariable String tagName) {
        try {
            Question question = questionService.addTagToQuestion(id, tagName);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}/tags/{tagName}")
    @Operation(summary = "Remove tag from question", description = "Remove a tag from an existing question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag removed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question or tag not found")
    })
    public ResponseEntity<Question> removeTagFromQuestion(
            @Parameter(description = "Question ID") @PathVariable Integer id,
            @Parameter(description = "Tag name") @PathVariable String tagName) {
        try {
            Question question = questionService.removeTagFromQuestion(id, tagName);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/tags")
    @Operation(summary = "Set question tags", description = "Replace all tags for a question with the provided set")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Question> setQuestionTags(
            @Parameter(description = "Question ID") @PathVariable Integer id,
            @Parameter(description = "Set of tag names") @RequestBody Set<String> tagNames) {
        try {
            Question question = questionService.setQuestionTags(id, tagNames);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DTO class for request body
    @Data
    @Schema(description = "Request object for creating or updating a question")
    public static class QuestionRequest {
        @Schema(description = "The question text", example = "What is the time complexity of binary search?")
        @NotBlank(message = "Question text is required")
        private String questionText;
        
        @Schema(description = "The answer to the question", example = "O(log n) - Binary search eliminates half of the search space in each iteration.")
        @NotBlank(message = "Question answer is required")
        private String questionAnswer;
        
        @Schema(description = "Difficulty level of the question", example = "MEDIUM")
        private Question.DifficultyLevel difficulty = Question.DifficultyLevel.MEDIUM;
    }
} 