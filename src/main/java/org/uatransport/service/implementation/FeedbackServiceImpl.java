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
import org.uatransport.entity.dto.HeatMapDTO;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.FeedbackRepository;
import org.uatransport.service.FeedbackService;
import org.uatransport.service.StopService;
import org.uatransport.service.converter.impl.FeedbackTypeConverter;
import org.uatransport.service.converter.impl.RatingConversionStrategy;
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

    // Old version
    @Override
    @Transactional(readOnly = true)
    public Double getAverageRateByTransitId(Integer transitId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.RATING);
        return getAverageRate(feedbackList);
    }

    // Old version
    @Override
    @Transactional(readOnly = true)
    public Double getAverageRateByTransitAndUser(Integer transitId, Integer userId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteriaAndUserId(transitId,
            FeedbackCriteria.FeedbackType.RATING, userId);
        return getAverageRate(feedbackList);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRateForRateAnswersByTransitAndUser(Integer transitId, Integer userId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteriaAndUserId(transitId,
            FeedbackCriteria.FeedbackType.RATING_ANSWER, userId);
        return getAverageRateForRateAnswers(feedbackList);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRateForRateAnswersByTransitId(Integer transitId) {
        List<Feedback> feedbackList = getByTransitAndFeedbackCriteria(transitId,
            FeedbackCriteria.FeedbackType.RATING_ANSWER);
        return getAverageRateForRateAnswers(feedbackList);
    }

    /**
     * Method to return map for HeatMap diagram on UI.
     *
     * @param transitId id of specified transit
     */
    @Override
    public List<HeatMapDTO> getHeatMap(Integer transitId) {
        List<Stop> stopList = stopService.getByTransitId(transitId);
        Map<String, Double> capacityMap = new TreeMap<>(Comparator
            .comparingInt(street -> stopService.getIndexByTransitIdAndStopName(transitId, street)));

        Map<Integer, Double> hourCapacityMap = getHourCapacityMap(transitId);
        int averageHourCapacity = hourCapacityMap.values().stream().mapToInt(Number::intValue).sum();
        Map<Stop, Double> stopCapacityMap = getStopCapacityMap(transitId);

        return valueToReturn(stopList, capacityMap, hourCapacityMap, averageHourCapacity, stopCapacityMap);
    }


    @Override
    @Transactional(readOnly = true)
    public Map<Stop, Double> getStopCapacityMap(Integer transitId, Stop... stops) {
        List<Stop> stopList = stops.length > 0 ? Arrays.asList(stops) : stopService.getByTransitId(transitId);
        Map<Stop, Double> capacityMap = new TreeMap<>(Comparator
            .comparingInt(stop -> stopService.getIndexByTransitIdAndStopName(transitId, stop.getStreet())));
        for (Stop stop : stopList) {
            capacityMap.put(stop, getCapacityByTransitAndStops(transitId, stop));
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

    /**
     * Method to create specific form of list of data for heatmap.
     */
    private List<HeatMapDTO> valueToReturn(List<Stop> stopList, Map<String, Double> capacityMap,
                                           Map<Integer, Double> hourCapacityMap, int averageHourCapacity, Map<Stop, Double> stopCapacityMap) {
        List<HeatMapDTO> valueToReturn = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            HeatMapDTO heatMapDTO = new HeatMapDTO();

            Double capacityFromHourCapacityMap = hourCapacityMap.get(i);

            mapGegeration(stopList, capacityMap, averageHourCapacity, stopCapacityMap, capacityFromHourCapacityMap);

            heatMapDTOSetName(heatMapDTO, i);

            heatMapDTO.setSeries(capacityMap);

            valueToReturn.add(heatMapDTO);
        }

        return valueToReturn;
    }

    /**
     * Method to set name to heatMapDTO in correct form (e.g. 00:00).
     *
     * @param heatMapDTO object in which should put specified name
     * @param i          exact name of the object
     */
    private void heatMapDTOSetName(HeatMapDTO heatMapDTO, int i) {
        if (i < 10) {
            heatMapDTO.setName("0" + i + ":00");
        } else {
            heatMapDTO.setName(i + ":00");
        }
    }

    /**
     * Method to write proper data to the capacity map
     */
    private void mapGegeration(List<Stop> stopList, Map<String, Double> capacityMap, int averageHourCapacity,
                               Map<Stop, Double> stopCapacityMap, Double capacityFromHourCapacityMap) {
        for (Stop stop : stopList) {
            Double valueToSaveInMap;

            if (averageHourCapacity != 0) {
                valueToSaveInMap = (stopCapacityMap.get(stop) * capacityFromHourCapacityMap) / averageHourCapacity;
            } else {
                valueToSaveInMap = (double) 0;
            }

            capacityMap.put(stop.getStreet(), valueToSaveInMap);
        }
    }

    private Double getAverageCapacityByTransitAndHour(Integer transitId, Integer feedbackHour) {
        return convertCapacityHourFeedBacks(transitId).stream()
            .filter(capacityHourFeedback -> capacityHourFeedback.containsHour(feedbackHour))
            .mapToInt(CapacityHourFeedback::getCapacity).average().orElse(0.0);
    }

    // Old version
    private Double getAverageRate(List<Feedback> feedbackList) {
        return feedbackList.stream().mapToInt(new RatingConversionStrategy()::convert).average()
            .orElseThrow(ResourceNotFoundException::new);
    }

    private Double getAverageRateForRateAnswers(List<Feedback> feedbackList) {
        return feedbackList.stream().mapToDouble(new RatingConversionStrategy()::apply).average()
            .orElseThrow(ResourceNotFoundException::new);
    }

    private List<CapacityHourFeedback> convertCapacityHourFeedBacks(Integer transitId) {

        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.HOURS_CAPACITY).stream()
            .map(feedback -> new FeedbackTypeConverter<>(CapacityHourFeedback.class).convert(feedback))
            .collect(Collectors.toList());
    }

    private boolean existInStopIndexesRange(Integer transitId, Stop stop, String fromStop, String toStop) {
        Integer fromStopIndex = stopService.getIndexByTransitIdAndStopName(transitId, fromStop);
        Integer toStopIndex = stopService.getIndexByTransitIdAndStopName(transitId, toStop);
        Integer stopIndex = stopService.getIndexByTransitIdAndStopName(transitId, stop.getStreet());

        return Range.closed(fromStopIndex, toStopIndex).contains(stopIndex);
    }

    private Double getCapacityByTransitAndStops(Integer transitId, Stop stop) {
        Predicate<CapacityRouteFeedback> existInRange = capacityHourFeedback -> existInStopIndexesRange(transitId, stop,
            capacityHourFeedback.getFrom().getStreet(), capacityHourFeedback.getTo().getStreet());

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
