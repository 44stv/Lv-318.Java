package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.Stop;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.entity.dto.HeatMapDTO;
import org.uatransport.service.FeedbackService;
import org.uatransport.service.converter.model.AccepterFeedback;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(params = "criteriaId")
    public List<Feedback> getByCriteria(@RequestParam("criteriaId") Integer criteriaId) {
        return feedbackService.getByCriteriaId(criteriaId);
    }

    @GetMapping(params = "transitId")
    public List<Feedback> getByTransit(@RequestParam("transitId") Integer transitId) {
        return feedbackService.getByTransitId(transitId);
    }

    @GetMapping(params = "userId")
    public List<Feedback> getByUser(@RequestParam("userId") Integer userId) {
        return feedbackService.getByUserId(userId);
    }

    @GetMapping(value = "/{id}")
    public Feedback getById(@PathVariable Integer id) {
        return feedbackService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Feedback> add(@RequestBody FeedbackDTO feedbackDTO) {
        return new ResponseEntity<>(feedbackService.addFeedback(feedbackDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/rating/{transitId}")
    public Double getRatingByTransit(@PathVariable Integer transitId) {
        return feedbackService.getRatingByTransitId(transitId);
    }

    @GetMapping(value = "/rating/{transitId}/{userId}")
    public Double getRatingByTransitAndUser(@PathVariable Integer transitId, @PathVariable Integer userId) {
        return feedbackService.getRatingByTransitAndUser(transitId, userId);
    }

    @GetMapping(value = "/byHour/{transitId}")
    public Map<Integer, Double> getCapacityHoursMap(@PathVariable Integer transitId) {
        return feedbackService.getHourCapacityMap(transitId);
    }

    @GetMapping(value = "/byStops/{transitId}/direction/{direction}")
    public Map<Stop, Double> getCapacityStopMap(@PathVariable Integer transitId,@PathVariable String direction,
                                                @RequestParam(value = "stop-list", required = false) List<Stop> stopList) {
        Stop[] stopsVarArg = stopList.toArray(new Stop[stopList.size()]);
        return feedbackService.getStopCapacityMap(transitId,direction, stopsVarArg);
    }

    @GetMapping(value = "/accepterMap/{transitId}")
    public EnumMap<AccepterFeedback, Double> getAccepterMap(@PathVariable Integer transitId) {
        return feedbackService.getAccepterAnswerPercentageMap(transitId);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<List<Feedback>> addAll(@RequestBody List<FeedbackDTO> feedbackDTOList) {
        return new ResponseEntity<>(feedbackService.addAll(feedbackDTOList), HttpStatus.CREATED);
    }


    /**
     * Method to returns data for the heatmap in single transit page.
     *
     * @param transitId id of specified transit
     */
    @GetMapping(value = "/heat-map/{transitId}")
    public List<HeatMapDTO> getHeatMapData(@PathVariable Integer transitId) {
        return feedbackService.getHeatMap(transitId);
    }
}
