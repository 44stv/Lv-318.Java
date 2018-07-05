package org.uatransport.service.implementation;

import com.google.common.collect.Range;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.Stop;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.FeedbackRepository;
import org.uatransport.service.FeedbackService;
import org.uatransport.service.StopService;
import org.uatransport.service.converter.impl.FeedbackTypeConverter;
import org.uatransport.service.converter.impl.RatingConverter;
import org.uatransport.service.converter.model.AccepterFeedback;
import org.uatransport.service.converter.model.CapacityHourFeedback;
import org.uatransport.service.converter.model.CapacityRouteFeedback;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final StopService stopService;

    @Override
    public Feedback addFeedback(FeedbackDTO feedbackDTO) {
        if (feedbackDTO == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return feedbackRepository.save(feedbackDTO.convertToEntity());
    }

    @Override
    public List<Feedback> addAll(List<FeedbackDTO> feedbackDTOList) {
        return Streams.stream(feedbackRepository.saveAll(FeedbackDTO.toEntity(feedbackDTOList)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getById(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Feedback with id '%s' not found", id)));
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
    public List<Feedback> getByTransitAndFeedbackCriteria(Integer transitId,
            FeedbackCriteria.FeedbackType feedbackType) {
        return feedbackRepository.findByTransitIdAndFeedbackCriteriaType(transitId, feedbackType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getByTransitAndFeedbackCriteriaAndUserId(Integer transitId,
            FeedbackCriteria.FeedbackType feedbackType, Integer userId) {
        return feedbackRepository.findByTransitIdAndFeedbackCriteriaTypeAndUserId(transitId, feedbackType, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRatingByTransitId(Integer transitId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.RATING);
        return getAverageRate(feedbackList);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRatingByTransitAndUser(Integer transitId, Integer userId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteriaAndUserId(transitId,
                FeedbackCriteria.FeedbackType.RATING, userId);
        return getAverageRate(feedbackList);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Stop, Double> getStopCapacityMap(Integer transitId, String direction, Stop... stops) {
        List<Stop> stopList = stops.length > 0 ? Arrays.asList(stops)
                : stopService.getByTransitIdAndDirection(transitId, direction);
        Map<Stop, Double> capacityMap = new TreeMap<>(Comparator.comparingInt(stop -> stopService
                .getIndexByTransitIdAndStopNameAndDirection(transitId, stop.getStreet(), direction)));
        for (Stop stop : stopList) {
            capacityMap.put(stop, getCapacityByTransitAndStops(transitId, stop, direction));
        }

        return capacityMap;
    }

    @Override
    @Transactional(readOnly = true)
    public EnumMap<AccepterFeedback, Double> getAccepterAnswerPercentageMap(Integer transitId) {
        EnumMap<AccepterFeedback, Double> accepterMap = new EnumMap<>(AccepterFeedback.class);
        for (AccepterFeedback accepterFeedback : AccepterFeedback.values()) {
            double percentValue = 100 * countByValue(accepterFeedback, transitId)
                    / (double) countAllAccepterFeedBacks(transitId);
            accepterMap.put(accepterFeedback, percentValue);
        }
        return accepterMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Double> getHourCapacityMap(Integer transitId) {
        Map<Integer, Double> capacityMap = new TreeMap<>();
        for (int hour = 0; hour < 24; hour++) {
            capacityMap.put(hour, getAverageCapacityByTransitAndHour(transitId, hour));
        }
        return capacityMap;
    }

    private Double getAverageCapacityByTransitAndHour(Integer transitId, Integer feedbackHour) {
        return convertCapacityHourFeedBacks(transitId).stream()
                .filter(capacityHourFeedback -> capacityHourFeedback.containsHour(feedbackHour))
                .mapToInt(CapacityHourFeedback::getCapacity).average().orElse(0.0);
    }

    private Double getAverageRate(List<Feedback> feedbackList) {
        return feedbackList.stream().mapToDouble(new RatingConverter()::convert).average().orElse(0.0);
    }

    private List<CapacityHourFeedback> convertCapacityHourFeedBacks(Integer transitId) {

        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.HOURS_CAPACITY).stream()
                .map(feedback -> new FeedbackTypeConverter<>(CapacityHourFeedback.class).convert(feedback))
                .collect(Collectors.toList());
    }

    private boolean existInStopIndexesRange(Integer transitId, Stop stop, String fromStop, String toStop,
            String direction) {
        Integer fromStopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, fromStop, direction);
        Integer toStopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, toStop, direction);
        Integer stopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, stop.getStreet(),
                direction);

        return (fromStopIndex < toStopIndex) ? Range.closed(fromStopIndex, toStopIndex).contains(stopIndex)
                : Range.closed(toStopIndex, fromStopIndex).contains(stopIndex);
    }

    private Double getCapacityByTransitAndStops(Integer transitId, Stop stop, String direction) {
        Predicate<CapacityRouteFeedback> existInRange = capacityRouteFeedback -> existInStopIndexesRange(transitId,
                stop, capacityRouteFeedback.getFrom().getStreet(), capacityRouteFeedback.getTo().getStreet(),
                direction);

        return convertCapacityRouteFeedBacks(transitId).stream().filter(existInRange)
                .mapToInt(CapacityRouteFeedback::getCapacity).average().orElse(0.0);
    }

    private List<CapacityRouteFeedback> convertCapacityRouteFeedBacks(Integer transitId) {
        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.ROUTE_CAPACITY).stream()
                .map(feedback -> new FeedbackTypeConverter<>(CapacityRouteFeedback.class).convert(feedback))
                .collect(Collectors.toList());
    }

    private Long countByValue(AccepterFeedback answer, Integer transitId) {
        return convertAccepterFeedBacks(transitId).stream().filter(accepterFeedback -> accepterFeedback == answer)
                .count();
    }

    private List<AccepterFeedback> convertAccepterFeedBacks(Integer transitId) {
        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.ACCEPTER).stream()
                .map(feedback -> new FeedbackTypeConverter<>(AccepterFeedback.class).convert(feedback))
                .collect(Collectors.toList());
    }

    private Integer countAllAccepterFeedBacks(Integer transitId) {
        return convertAccepterFeedBacks(transitId).size();
    }
}