package com.platform.recalldev.service;

import com.platform.recalldev.entity.Question;
import com.platform.recalldev.entity.Tag;
import com.platform.recalldev.repository.QuestionRepository;
import com.platform.recalldev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    
    @Autowired
    public QuestionService(QuestionRepository questionRepository, TagRepository tagRepository) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
    }
    
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    
    public List<Question> getAllQuestionsWithTags() {
        return questionRepository.findAllWithTags();
    }
    
    public Optional<Question> getQuestionById(Integer id) {
        return questionRepository.findById(id);
    }
    
    public Question getQuestionByIdWithTags(Integer id) {
        return questionRepository.findByIdWithTags(id);
    }
    
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    public Question createQuestion(String questionText, String questionAnswer, Question.DifficultyLevel difficulty) {
        Question question = Question.builder()
                .questionText(questionText)
                .questionAnswer(questionAnswer)
                .difficulty(difficulty)
                .build();

        return questionRepository.save(question);
    }
    
    public Question updateQuestion(Integer id, String questionText, String questionAnswer, Question.DifficultyLevel difficulty) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        question.setQuestionText(questionText);
        question.setQuestionAnswer(questionAnswer);
        question.setDifficulty(difficulty);
        
        return questionRepository.save(question);
    }
    
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
    
    public List<Question> getQuestionsByDifficulty(Question.DifficultyLevel difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }
    
    public List<Question> searchQuestions(String keyword) {
        return questionRepository.findByKeyword(keyword);
    }
    
    public List<Question> getQuestionsByTag(String tagName) {
        return questionRepository.findByTagName(tagName);
    }
    
    public List<Question> getQuestionsByTags(List<String> tagNames) {
        return questionRepository.findByTagNames(tagNames);
    }
    
    public Question addTagToQuestion(Integer questionId, String tagName) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
        
        question.addTag(tag);
        return questionRepository.save(question);
    }
    
    public Question removeTagFromQuestion(Integer questionId, String tagName) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found with name: " + tagName));
        
        question.removeTag(tag);
        return questionRepository.save(question);
    }
    
    public Question setQuestionTags(Integer questionId, Set<String> tagNames) {
        Question question = questionRepository.findByIdWithTags(questionId);
        if (question == null) {
            throw new RuntimeException("Question not found with id: " + questionId);
        }
        
        // Clear existing tags
        question.getTags().clear();
        
        // Add new tags
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
            question.addTag(tag);
        }
        
        return questionRepository.save(question);
    }
} 