package org.uatransport.repository;

import org.springframework.data.repository.CrudRepository;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.FeedbackCriteria;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {

    List<Feedback> findByTransitId(Integer id);

    List<Feedback> findByUserId(Integer id);

    List<Feedback> findByFeedbackCriteriaId(Integer id);

    List<Feedback> findByTransitIdAndFeedbackCriteriaType(Integer transitId, FeedbackCriteria.FeedbackType feedbackType);

    List<Feedback> findByTransitIdAndFeedbackCriteriaTypeAndUserId(Integer transitId, FeedbackCriteria.FeedbackType feedbackType,Integer userId);
}
