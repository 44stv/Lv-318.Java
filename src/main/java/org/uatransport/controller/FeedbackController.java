package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.service.FeedbackService;
import org.uatransport.service.model.RouteBusyHoursFeedback;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@CrossOrigin
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(params = "criteriaId")
    public ResponseEntity<List<Feedback>> getByCriteria(@RequestParam("criteriaId") Integer criteriaId) {
        return new ResponseEntity<>(feedbackService.getByCriteriaId(criteriaId), HttpStatus.OK);
    }

    @GetMapping(params = "transitId")
    public ResponseEntity<List<Feedback>> getByTransit(@RequestParam("transitId") Integer transitId) {

        return new ResponseEntity<>(feedbackService.getByTransitId(transitId), HttpStatus.OK);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<Feedback>> getByUser(@RequestParam("userId") Integer userId) {
        return new ResponseEntity<>(feedbackService.getByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Feedback> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(feedbackService.getById(id), HttpStatus.OK);
    }


    @PostMapping(value = "/add")
    public ResponseEntity<List<Feedback>> addAll(@RequestBody List<FeedbackDTO> feedbackDTOList) {
        return new ResponseEntity<>(feedbackService.addAll(feedbackDTOList), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Feedback> add(@RequestBody FeedbackDTO feedbackDTO) {
        return new ResponseEntity<>(feedbackService.addFeedback(feedbackDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/rate/{transitId}")
    public ResponseEntity<Double> getAverageRateByTransit(@PathVariable Integer transitId) {
        return new ResponseEntity<>(feedbackService.convertRatingFeedBacks(transitId), HttpStatus.OK);
    }

    @GetMapping(value = "/rate/{transitId}/{userId}")
    public ResponseEntity<Double> getAverageRateByTransitAndUser(@PathVariable Integer transitId, @PathVariable Integer userId) {
        return new ResponseEntity<>(feedbackService.convertRatingFeedBacksByUser(transitId, userId), HttpStatus.OK);
    }

    @GetMapping(value = "/route/{transitId}")
    public ResponseEntity<List<RouteBusyHoursFeedback>> getRouteBusyHoursFeedBacks(@PathVariable Integer transitId) {
        return new ResponseEntity<>(feedbackService.convertRouteBusyHoursFeedBacks(transitId), HttpStatus.OK);
    }

    //    @GetMapping(value = "/capacity/{transitId}")
//    public ResponseEntity<List<CapacityBusyHoursFeedback>> getCapacityFeedBacks(@PathVariable Integer transitId) {
//        return new ResponseEntity<>(feedbackService.convertCapacityFeedBacks(transitId), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/accepter/{transitId}")
//    public ResponseEntity<List<AccepterFeedback>> getAccepterFeedBacks(@PathVariable Integer transitId) {
//        return new ResponseEntity<>(feedbackService.convertAccepterFeedBacks(transitId), HttpStatus.OK);
//    }
    @GetMapping(value = "/capacity/{transitId}")
    public ResponseEntity<Map<Integer, Double>> getCapacityMap(@PathVariable Integer transitId) {
        return new ResponseEntity<>(feedbackService.getCapacityMap(transitId), HttpStatus.OK);
    }
}