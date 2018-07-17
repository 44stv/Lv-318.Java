package org.uatransport.service;

import org.uatransport.entity.Feedback;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.Stop;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.entity.dto.HeatMapDTO;
import org.uatransport.service.converter.model.SimpleFeedback;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface FeedbackService {

    List<Feedback> addAll(List<FeedbackDTO> feedbackDTOList);

    Feedback addFeedback(FeedbackDTO feedbackDTO);

    Feedback getById(Integer id);

    List<Feedback> getByTransitId(Integer id);

    List<Feedback> getByCriteriaId(Integer id);

    List<Feedback> getByUserId(Integer id);

    List<Feedback> getByTransitIdAndFeedbackCriteriaType(Integer transitId, FeedbackCriteria.FeedbackType feedbackType);

    List<Feedback> getByTransitCategoryIdAndFeedbackCriteriaType(Integer transitCategoryId,
                                                                 FeedbackCriteria.FeedbackType feedbackType);

    List<Feedback> getByTransitIdAndFeedbackCriteriaTypeAndUserId(Integer transitId,
                                                                  FeedbackCriteria.FeedbackType feedbackType, Integer userId);

    Double getRatingByTransitId(Integer transitId);

    Double getRatingByTransitCategoryId(Integer transitCategoryId);

    Double getRatingByTransitIdAndUserId(Integer transitId, Integer userId);

    Map<Integer, Double> getHourCapacityMap(Integer transitId);

    Map<Stop, Double> getStopCapacityMap(Integer transitId, Stop.Direction direction, Stop... stops);

    EnumMap<SimpleFeedback, Double> getSimpleAnswerPercentageMap(Integer transitId);

    List<HeatMapDTO> getHeatMap(Integer transitId, Stop... stops);

}
