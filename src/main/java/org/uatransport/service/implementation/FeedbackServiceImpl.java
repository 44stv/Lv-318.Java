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
import org.uatransport.service.converter.impl.RatingConverter;
import org.uatransport.service.converter.model.QuantityLoadFeedback;
import org.uatransport.service.converter.model.SimpleFeedback;
import org.uatransport.service.converter.model.CapacityHourFeedback;
import org.uatransport.service.converter.model.CapacityRouteFeedback;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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
    public List<Feedback> getByTransitCategoryIdAndFeedbackCriteria(Integer transitCategoryId,
                                                                    FeedbackCriteria.FeedbackType feedbackType) {
        return feedbackRepository.findByTransitCategoryIdAndFeedbackCriteriaType(transitCategoryId, feedbackType);
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

        return getAverageRate(getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.RATING));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRatingByCategoryId(Integer categoryId) {

        return getAverageRate(getByTransitCategoryIdAndFeedbackCriteria(categoryId,
            FeedbackCriteria.FeedbackType.RATING));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRatingByTransitAndUser(Integer transitId, Integer userId) {

        List<Feedback> feedbackList = getByTransitAndFeedbackCriteriaAndUserId(transitId,
            FeedbackCriteria.FeedbackType.RATING, userId);
        List<Feedback> quantityFeedbackList = getByTransitAndFeedbackCriteriaAndUserId(transitId,
            FeedbackCriteria.FeedbackType.QUALITY, userId);

        return DoubleStream.of(getAverageRate(feedbackList), getAverageQualityRate(quantityFeedbackList))
            .average()
            .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Stop, Double> getStopCapacityMap(Integer transitId, Stop.DIRECTION direction, Stop... stops) {

        List<Stop> stopList = stops.length > 0 ? Arrays.asList(stops)
            : stopService.getByTransitIdAndDirection(transitId, direction);
        List<CapacityRouteFeedback> capacityRouteFeedbackList = convertCapacityRouteFeedBacks(transitId);

        Map<Stop, Double> capacityMap = new TreeMap<>(Comparator.comparingInt(stop -> stopService
            .getIndexByTransitIdAndStopNameAndDirection(transitId, stop.getStreet(), direction)));
        for (Stop stop : stopList) {
            capacityMap.put(stop, getCapacityByTransitAndStops(transitId, stop, direction, capacityRouteFeedbackList));
        }
        return capacityMap;
    }

    @Override
    @Transactional(readOnly = true)
    public EnumMap<SimpleFeedback, Double> getSimpleAnswerPercentageMap(Integer transitId) {

        EnumMap<SimpleFeedback, Double> simpleFeedbackDoubleEnumMap = new EnumMap<>(SimpleFeedback.class);

        for (SimpleFeedback simpleFeedback : SimpleFeedback.values()) {
            double percentValue = 100 * countByValue(simpleFeedback, transitId)
                / (double) countAllAccepterFeedBacks(transitId);
            simpleFeedbackDoubleEnumMap.put(simpleFeedback, percentValue);
        }

        return simpleFeedbackDoubleEnumMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Double> getHourCapacityMap(Integer transitId) {

        Map<Integer, Double> capacityMap = new TreeMap<>();
        List<CapacityHourFeedback> capacityHourFeedbackList = convertCapacityHourFeedBacks(transitId);

        for (int hour = 0; hour < 24; hour++) {
            capacityMap.put(hour, getAverageCapacityByHour(hour, capacityHourFeedbackList));
        }
        return capacityMap;
    }

    /**
     * Method to return map for HeatMap diagram on UI.
     *
     * @param transitId id of specified transit
     */
    @Override
    public List<HeatMapDTO> getHeatMap(Integer transitId) {
        List<Stop> stopList = stopService.getByTransitIdAndDirection(transitId, Stop.DIRECTION.FORWARD);
        Map<String, Double> capacityMap = new TreeMap<>(Comparator.comparingInt(street -> stopService
            .getIndexByTransitIdAndStopNameAndDirection(transitId, street, Stop.DIRECTION.FORWARD)));

        Map<Integer, Double> hourCapacityMap = getHourCapacityMap(transitId);
        int averageHourCapacity = hourCapacityMap.values().stream().mapToInt(Number::intValue).sum();
        Map<Stop, Double> stopCapacityMap = getStopCapacityMap(transitId, Stop.DIRECTION.FORWARD);

        return valueToReturn(stopList, capacityMap, hourCapacityMap, averageHourCapacity, stopCapacityMap);
    }


    /**
     * Method to return default value in case of absence of proper data.
     *
     * @param accepterMap EnumMap which should be checked
     */
    private EnumMap<SimpleFeedback, Double> returnAccepterMapNonZeroValue(EnumMap<SimpleFeedback, Double> accepterMap) {
        boolean isZero = false;
        AtomicReference<Double> valueInMap = new AtomicReference<>((double) 0);
        accepterMap.forEach((key, value) -> valueInMap.updateAndGet(v -> v + value));
        if (valueInMap.get() == 0) {
            accepterMap.put(SimpleFeedback.YES, (double) 1);
        }
        return accepterMap;
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
        // 10 is the first two-digit number
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

    private Double getAverageCapacityByHour(Integer feedbackHour, List<CapacityHourFeedback> capacityHourFeedbackList) {

        return capacityHourFeedbackList.stream()
            .filter(capacityHourFeedback -> capacityHourFeedback.containsHour(feedbackHour))
            .mapToInt(CapacityHourFeedback::getCapacity)
            .average()
            .orElse(0.0);
    }

    private Double getAverageRate(List<Feedback> feedbackList) {

        return feedbackList.stream()
            .mapToDouble(new RatingConverter()::convert)
            .average()
            .orElse(0.0);
    }

    private List<CapacityHourFeedback> convertCapacityHourFeedBacks(Integer transitId) {

        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.HOURS_CAPACITY).stream()
            .map(feedback -> new FeedbackTypeConverter<>(CapacityHourFeedback.class).convert(feedback))
            .collect(Collectors.toList());
    }

    private boolean existInStopIndexesRange(Integer transitId, Integer stopIndex,
                                            String fromStop, String toStop, Stop.DIRECTION direction) {
        Integer fromStopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, fromStop, direction);
        Integer toStopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, toStop, direction);

        return (fromStopIndex < toStopIndex) ? Range.closed(fromStopIndex, toStopIndex).contains(stopIndex)
            : Range.closed(toStopIndex, fromStopIndex).contains(stopIndex);
    }

    private Double getCapacityByTransitAndStops(Integer transitId, Stop stop, Stop.DIRECTION direction,
                                                List<CapacityRouteFeedback> capacityRouteFeedbackList) {
        Integer stopIndex = stopService.getIndexByTransitIdAndStopNameAndDirection(transitId, stop.getStreet(), direction);
        Predicate<CapacityRouteFeedback> existInRange = capacityRouteFeedback -> existInStopIndexesRange(transitId,
            stopIndex, capacityRouteFeedback.getFrom().getStreet(), capacityRouteFeedback.getTo().getStreet(),
            direction);

        return capacityRouteFeedbackList.stream()
            .filter(existInRange)
            .mapToInt(CapacityRouteFeedback::getCapacity)
            .average()
            .orElse(0.0);
    }

    private List<CapacityRouteFeedback> convertCapacityRouteFeedBacks(Integer transitId) {

        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.ROUTE_CAPACITY).stream()
            .map(feedback -> new FeedbackTypeConverter<>(CapacityRouteFeedback.class).convert(feedback))
            .collect(Collectors.toList());
    }

    private Long countByValue(SimpleFeedback answer, Integer transitId) {

        return convertSimpleFeedBacks(transitId).stream()
            .filter(simpleFeedback -> simpleFeedback == answer)
            .count();
    }

    private List<SimpleFeedback> convertSimpleFeedBacks(Integer transitId) {
        return getByTransitAndFeedbackCriteria(transitId, FeedbackCriteria.FeedbackType.SIMPLE).stream()
            .map(feedback -> new FeedbackTypeConverter<>(SimpleFeedback.class).convert(feedback))
            .collect(Collectors.toList());
    }

    private Integer countAllAccepterFeedBacks(Integer transitId) {
        return convertSimpleFeedBacks(transitId).size();
    }

    private Double getAverageQualityRate(List<Feedback> feedbackList) {
        return feedbackList.stream()
            .map(feedback -> new FeedbackTypeConverter<>(QuantityLoadFeedback.class).convert(feedback))
            .mapToInt(QuantityLoadFeedback::getRate)
            .average()
            .orElse(0.0);
    }


}
