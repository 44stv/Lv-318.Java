package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.Question;
import org.uatransport.service.FeedbackCriteriaService;
import org.uatransport.service.QuestionService;

import javax.management.Query;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback-criteria")
public class FeedbackCriteriaController {
    private final FeedbackCriteriaService feedbackCriteriaService;
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<FeedbackCriteria> addFeedbackCriteria(@RequestBody FeedbackCriteria feedbackCriteria) {
        return new ResponseEntity<>(feedbackCriteriaService.save(feedbackCriteria), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedbackCriteria(@PathVariable Integer id) {
        feedbackCriteriaService.delete(id);
    }

    @PutMapping("/{id}")
    public FeedbackCriteria updateFeedbackCriteria(@RequestBody FeedbackCriteria feedbackCriteria,
            @PathVariable Integer id) {
        return feedbackCriteriaService.update(feedbackCriteria.setId(id));
    }

    @GetMapping
    public List<FeedbackCriteria> getAllFeedbackCriteria() {
        return feedbackCriteriaService.getAll();
    }

    @GetMapping("/{id}")
    public FeedbackCriteria getById(@PathVariable Integer id) {
        return feedbackCriteriaService.getById(id);
    }

    @GetMapping(params = "type")
    public List<FeedbackCriteria> getByType(@RequestParam(value = "type") FeedbackCriteria.FeedbackType type) {
        return feedbackCriteriaService.getByType(type);
    }

    @GetMapping(params = "categoryId")
    public List<FeedbackCriteria> getByCategoryId(@RequestParam(value = "categoryId") Integer categoryId) {
        return feedbackCriteriaService.getByCategoryId(categoryId);
    }

    @GetMapping(params = "questionId")
    public List<FeedbackCriteria> getByQuestionsId(@RequestParam(value = "questionId") Integer questionId) {
        return feedbackCriteriaService.getByQuestionsId(questionId);

    }

    @GetMapping(params = "questionType")
    public List<FeedbackCriteria> getByQuestionsType(
            @RequestParam(value = "questionType") Question.QuestionType questionType) {
        return feedbackCriteriaService.getByQuestionsType(questionType);

    }

    @GetMapping(params = "question")
    public List<FeedbackCriteria> getByQuestions(@RequestParam(value = "question") String question) {
        return feedbackCriteriaService.getByQuestionsName(question);
    }

    @GetMapping(params = "weight")
    public List<FeedbackCriteria> getByWeight(@RequestParam("weight") Integer weight) {
        return feedbackCriteriaService.getByQuestionsWeight(weight);

    }

    @GetMapping("/categoryId/{categoryId}/type/{type}")
    public List<FeedbackCriteria> getByTypeAndCategoryId(@PathVariable(value = "categoryId") Integer categoryId,
            @PathVariable(value = "type") String type) {
        return feedbackCriteriaService.getByTypeAndCategoryId(categoryId, type);
    }

    @GetMapping("/enums")
    public List<String> getAllEnumTypes() {
        return feedbackCriteriaService.getAllEnumsType();
    }

    @GetMapping("/questionEnums")
    public List<String> getAllEnumTypesOfQuestion() {
        return questionService.getAllEnumsTypeOfQuestion();
    }
}
