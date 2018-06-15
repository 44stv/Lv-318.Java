package org.uatransport.service;

import org.uatransport.entity.Feedback;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.service.model.AccepterFeedback;
import org.uatransport.service.model.CapacityBusyHoursFeedback;
import org.uatransport.service.model.RouteBusyHoursFeedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FeedbackService {

    List<Feedback> addAll(List<FeedbackDTO> feedbackDTOList);

    Feedback addFeedback(FeedbackDTO feedbackDTO);

    Feedback getById(Integer id);

    List<Feedback> getByTransitId(Integer id);

    List<Feedback> getByCriteriaId(Integer id);

    List<Feedback> getByUserId(Integer id);

    List<Feedback> getByTransitAndFeedbackCriteria(Integer transitId, FeedbackCriteria.FeedbackType feedbackType);

    List<Feedback> getByTransitAndFeedbackCriteriaAndUserId(Integer transitId, FeedbackCriteria.FeedbackType feedbackType, Integer userId);

    Double convertRatingFeedBacks(Integer transitId);

    Double convertRatingFeedBacksByUser(Integer transitId, Integer userId);

    List<RouteBusyHoursFeedback> convertRouteBusyHoursFeedBacks(Integer transitId);

    List<CapacityBusyHoursFeedback> convertCapacityFeedBacks(Integer transitId);

    List<AccepterFeedback> convertAccepterFeedBacks(Integer transitId);

    Map<Integer, Double> getCapacityMap(Integer transitId);

}
