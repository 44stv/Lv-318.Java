// package org.uatransport.service;
//
// import org.assertj.core.util.Sets;
// import org.hamcrest.Matchers;
// import org.hamcrest.collection.IsMapContaining;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.uatransport.entity.Feedback;
// import org.uatransport.entity.Stop;
// import org.uatransport.entity.Stop.Direction;
// import org.uatransport.entity.dto.FeedbackDTO;
// import org.uatransport.exception.ResourceNotFoundException;
// import org.uatransport.repository.FeedbackRepository;
// import org.uatransport.repository.StopRepository;
// import org.uatransport.service.converter.model.SimpleFeedback;
// import org.uatransport.service.implementation.FeedbackServiceImpl;
// import org.uatransport.service.implementation.StopServiceImpl;
//
// import java.util.*;
//
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;
// import static org.junit.Assert.assertThat;
// import static org.uatransport.service.converter.model.SimpleFeedback.*;
//
// @RunWith(SpringRunner.class)
// @DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// public class FeedbackServiceTest {
//
// private static final Double EXPECTED_ZERO_VALUE = 0.0;
//
// private static final Integer EXIST_DB_USER_ID = 1;
// private static final Integer NON_EXIST_DB_USER_ID = 6;
// private static final Integer EXIST_DB_TRANSIT_ID = 10365;
// private static final Integer NON_EXIST_DB_TRANSIT_ID = 15;
// private static final Integer EXIST_DB_CATEGORY_ID = 7;
// private static final Integer NON_EXIST_DB_CATEGORY_ID = 1;
//
// private FeedbackService feedbackService;
// private StopService stopService;
//
// @Autowired
// private FeedbackRepository feedbackRepository;
//
// @Autowired
// private StopRepository stopRepository;
//
// @Before
// public void setUp() {
// stopService = new StopServiceImpl(stopRepository);
// feedbackService = new FeedbackServiceImpl(feedbackRepository, stopService);
// }
//
// @Test(expected = ResourceNotFoundException.class)
// public void getById() {
// feedbackRepository.delete(new Feedback().setId(1));
// feedbackService.getById(1);
// }
//
// @Test
// public void getRatingByTransitIdTest() {
// Double actualNotZeroRate = feedbackService.getRatingByTransitId(EXIST_DB_TRANSIT_ID);
// assertNotEquals(EXPECTED_ZERO_VALUE, actualNotZeroRate);
//
// Double actualZeroRate = feedbackService.getRatingByTransitId(NON_EXIST_DB_TRANSIT_ID);
// assertEquals(EXPECTED_ZERO_VALUE, actualZeroRate, 0.0);
// }
//
// @Test
// public void getRatingByTransitAndUserTest() {
// Double actualRate = feedbackService.getRatingByTransitIdAndUserId(EXIST_DB_TRANSIT_ID, EXIST_DB_USER_ID);
// assertNotEquals(EXPECTED_ZERO_VALUE, actualRate);
//
// Double actualRateMissedUser = feedbackService.getRatingByTransitIdAndUserId(EXIST_DB_TRANSIT_ID,
// NON_EXIST_DB_USER_ID);
// assertEquals(EXPECTED_ZERO_VALUE, actualRateMissedUser, 0.0);
//
// Double actualRateMissedTransit = feedbackService.getRatingByTransitIdAndUserId(NON_EXIST_DB_TRANSIT_ID,
// EXIST_DB_USER_ID);
// assertEquals(EXPECTED_ZERO_VALUE, actualRateMissedTransit, 0.0);
// }
//
// @Test
// public void getRatingByCategoryIdTest() {
// Double actualNotZeroRate = feedbackService.getRatingByTransitCategoryId(EXIST_DB_CATEGORY_ID);
// assertNotEquals(EXPECTED_ZERO_VALUE, actualNotZeroRate);
//
// Double actualZeroRate = feedbackService.getRatingByTransitCategoryId(NON_EXIST_DB_CATEGORY_ID);
// assertEquals(EXPECTED_ZERO_VALUE, actualZeroRate, 0.0);
// }
//
// @Test
// public void getStopCapacityMapTest() {
// Set<Stop> expectedStops = Sets
// .newHashSet(stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.FORWARD));
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.FORWARD);
// assertEquals(expectedStops, stopCapacityMap.keySet());
// }
//
// @Test
// public void getStopCapacityMapForwardTest() {
// Set<Stop> expectedStops = Sets
// .newHashSet(stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.FORWARD));
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.FORWARD);
//
// assertThat(stopCapacityMap, IsMapContaining.hasValue(EXPECTED_ZERO_VALUE));
// assertEquals(expectedStops, stopCapacityMap.keySet());
// }
//
// @Test
// public void getStopCapacityMapForwardNoTransitTest() {
// Set<Stop> expectedStops = Collections.emptySet();
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(NON_EXIST_DB_TRANSIT_ID,
// Direction.FORWARD);
// assertEquals(expectedStops, stopCapacityMap.keySet());
// }
//
// @Test
// public void getStopCapacityMapBackwardTest() {
// Set<Stop> expectedStops = Sets
// .newHashSet(stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.BACKWARD));
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.BACKWARD);
// assertEquals(expectedStops, stopCapacityMap.keySet());
// }
//
// @Test
// public void getStopCapacityMapForwardVarargTest() {
// List<Stop> stopList = stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.FORWARD);
// List<Stop> trimmedStops = stopList.subList(0, stopList.size() / 2);
// Stop[] expectedStopVararg = trimmedStops.toArray(new Stop[trimmedStops.size()]);
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.FORWARD,
// expectedStopVararg);
// assertEquals(Sets.newHashSet(trimmedStops), stopCapacityMap.keySet());
// }
//
// @Test
// public void getStopCapacityMapBackwardVarargTest() {
// List<Stop> stopList = stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.BACKWARD);
// List<Stop> trimmedStops = stopList.subList(stopList.size() / 2, stopList.size());
// Stop[] expectedStopVararg = trimmedStops.toArray(new Stop[trimmedStops.size()]);
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.BACKWARD,
// expectedStopVararg);
// assertEquals(Sets.newHashSet(trimmedStops), stopCapacityMap.keySet());
// }
//
// @Test
// public void getSimpleAnswerPercentageMap() {
// Set<SimpleFeedback> expectedEnumSet = EnumSet.of(YES, NO, MAYBE);
// EnumMap<SimpleFeedback, Double> simpleFeedbackDoubleEnumMap = feedbackService
// .getSimpleAnswerPercentageMap(EXIST_DB_TRANSIT_ID);
// Double expectedNoValue = 100.0;
//
// assertEquals(expectedEnumSet, simpleFeedbackDoubleEnumMap.keySet());
// assertEquals(EXPECTED_ZERO_VALUE, simpleFeedbackDoubleEnumMap.get(MAYBE));
// assertEquals(expectedNoValue, simpleFeedbackDoubleEnumMap.get(NO));
// }
//
// @Test
// public void getStopCapacityMapStopIndexOrderTest() {
// List<Stop> stopList = stopService.getByTransitIdAndDirection(EXIST_DB_TRANSIT_ID, Direction.FORWARD).subList(0,
// 4);
// Collections.shuffle(stopList);
// Stop[] stopVararg = stopList.toArray(new Stop[stopList.size()]);
// Map<Stop, Double> stopCapacityMap = feedbackService.getStopCapacityMap(EXIST_DB_TRANSIT_ID, Direction.FORWARD,
// stopVararg);
//
// assertThat(stopCapacityMap.keySet(),
// Matchers.contains(stopList.get(0), stopList.get(1), stopList.get(2), stopList.get(3)));
// }
//
// @Test
// public void getHourCapacityMap() {
// Map<Integer, Double> hourCapacityMap = feedbackService.getHourCapacityMap(EXIST_DB_TRANSIT_ID);
//
// assertEquals(EXPECTED_ZERO_VALUE, hourCapacityMap.get(1));
// }
//
// }
