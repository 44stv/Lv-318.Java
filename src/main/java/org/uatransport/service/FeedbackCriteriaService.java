package org.uatransport.service;

import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.Question;

import java.util.List;

public interface FeedbackCriteriaService {
    FeedbackCriteria save(FeedbackCriteria feedbackCriteria);

    void delete(Integer id);

    FeedbackCriteria update(FeedbackCriteria feedbackCriteria);

    List<FeedbackCriteria> getAll();

    FeedbackCriteria getById(Integer id);

    List<FeedbackCriteria> getByType(FeedbackCriteria.FeedbackType type);

    List<FeedbackCriteria> getByCategoryId(Integer id);

    List<FeedbackCriteria> getByQuestionsName(String question);

    List<FeedbackCriteria> getByQuestionsId(Integer questionId);

    List<FeedbackCriteria> getByTypeAndCategoryId(Integer categoryId, String type);

    List<String> getAllEnumsType();

    List<FeedbackCriteria> getByQuestionsWeight(Integer weight);

    List<FeedbackCriteria> getByQuestionsType(Question.QuestionType type);

}
