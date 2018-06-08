package org.uaTransport.service.implementation;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uaTransport.entity.Feedback;
import org.uaTransport.entity.FeedbackCriteria;
import org.uaTransport.entity.dto.FeedbackDTO;
import org.uaTransport.exception.ResourceNotFoundException;
import org.uaTransport.repository.FeedbackRepository;
import org.uaTransport.service.FeedbackService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public Feedback addFeedback(Feedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return feedbackRepository.save(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getById(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Feedback with id '%s' not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getByTransitId(Integer id) {
        return feedbackRepository.findByTransitId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getByCriteriaId(Integer id) {
        return feedbackRepository.findByFeedbackCriteriaId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getByUserId(Integer id) {
        return feedbackRepository.findByUserId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getByTransitAndFeedbackCriteria(Integer transitId, FeedbackCriteria.FeedbackType feedbackType) {
        return feedbackRepository.findByTransitIdAndFeedbackCriteriaType(transitId, feedbackType);
    }

    @Override
    public List<Feedback> addAll(List<FeedbackDTO> feedbackDTOList) {
        return Streams.stream(feedbackRepository.saveAll(convertFromDTO(feedbackDTOList))).collect(Collectors.toList());

    }

    private List<Feedback> convertFromDTO(List<FeedbackDTO> feedbackDTOList) {
        return feedbackDTOList.stream()
                .map(FeedbackDTO::toEntity)
                .collect(Collectors.toList());
    }

    public List<Duration> convertBusyHoursFeedBacks(Integer transitId) {
        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.BUSY_HOURS)
                .stream()
                .<List<Duration>>map(FeedbackCriteria.FeedbackType.BUSY_HOURS::convertFeedback)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Double convertRatingFeedBacks(Integer transitId) {
        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.RATING)
                .stream()
                .mapToInt(FeedbackCriteria.FeedbackType.RATING::convertFeedback)
                .average()
                .orElseThrow(ResourceNotFoundException::new);
    }


}