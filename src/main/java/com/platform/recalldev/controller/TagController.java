package com.platform.recalldev.controller;

import com.platform.recalldev.entity.Tag;
import com.platform.recalldev.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "API for managing question tags")
public class TagController {
    
    private final TagService tagService;
    
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    
    @GetMapping
    @Operation(summary = "Get all tags", description = "Retrieve all tags with optional question inclusion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tags",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class)))
    })
    public ResponseEntity<List<Tag>> getAllTags(
            @Parameter(description = "Include questions in the response") 
            @RequestParam(required = false) boolean includeQuestions) {
        List<Tag> tags = includeQuestions ? 
                tagService.getAllTagsWithQuestions() : 
                tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID", description = "Retrieve a specific tag by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Tag> getTagById(
            @Parameter(description = "Tag ID") @PathVariable Integer id,
            @Parameter(description = "Include questions in the response") 
            @RequestParam(required = false) boolean includeQuestions) {
        if (includeQuestions) {
            Tag tag = tagService.getTagByIdWithQuestions(id);
            return tag != null ? ResponseEntity.ok(tag) : ResponseEntity.notFound().build();
        } else {
            Optional<Tag> tag = tagService.getTagById(id);
            return tag.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get tag by name", description = "Retrieve a specific tag by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Tag> getTagByName(
            @Parameter(description = "Tag name") @PathVariable String name) {
        Optional<Tag> tag = tagService.getTagByName(name);
        return tag.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new tag", description = "Create a new tag for categorizing questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or tag already exists")
    })
    public ResponseEntity<Tag> createTag(
            @Parameter(description = "Tag data") @Valid @RequestBody TagRequest request) {
        try {
            Tag tag = tagService.createTag(request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(tag);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a tag", description = "Update an existing tag by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or tag name already exists")
    })
    public ResponseEntity<Tag> updateTag(
            @Parameter(description = "Tag ID") @PathVariable Integer id,
            @Parameter(description = "Updated tag data") @Valid @RequestBody TagRequest request) {
        try {
            Tag tag = tagService.updateTag(id, request.getName());
            return ResponseEntity.ok(tag);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag", description = "Delete a tag by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "Tag ID") @PathVariable Integer id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search tags", description = "Search tags by keyword in name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class)))
    })
    public ResponseEntity<List<Tag>> searchTags(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<Tag> tags = tagService.searchTags(keyword);
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/with-questions")
    @Operation(summary = "Get tags with questions", description = "Retrieve tags that have at least one question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class)))
    })
    public ResponseEntity<List<Tag>> getTagsWithQuestions() {
        List<Tag> tags = tagService.getTagsWithQuestions();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/without-questions")
    @Operation(summary = "Get tags without questions", description = "Retrieve tags that have no questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class)))
    })
    public ResponseEntity<List<Tag>> getTagsWithoutQuestions() {
        List<Tag> tags = tagService.getTagsWithoutQuestions();
        return ResponseEntity.ok(tags);
    }
    
    // DTO class for request body
    @Data
    @Schema(description = "Request object for creating or updating a tag")
    public static class TagRequest {
        @Schema(description = "The tag name", example = "algorithms")
        @NotBlank(message = "Tag name is required")
        private String name;
    }
} 