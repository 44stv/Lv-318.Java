package org.uatransport.service;

import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.RatingCriteria;

import java.util.List;


public interface FeedbackCriteriaService {
    FeedbackCriteria save(FeedbackCriteria feedbackCriteria);

    void delete(Integer id);

    FeedbackCriteria update(FeedbackCriteria feedbackCriteria);

    List<FeedbackCriteria> getAll();

    FeedbackCriteria getById(Integer id);

    List<FeedbackCriteria> getByGroupId(Integer groupId);

    List<FeedbackCriteria> getByQuestion(String question);

    List<FeedbackCriteria> getByType(FeedbackCriteria.FeedbackType type);

    List<RatingCriteria> getByWeight(Integer id);

    List<FeedbackCriteria> getByCategoryId(Integer id);
}