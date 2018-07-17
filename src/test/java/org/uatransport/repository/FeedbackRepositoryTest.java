package org.uatransport.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.uatransport.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackRepositoryTest {

    private static final ExtendableCategory DEFAULT_DB_NON_EXTENDABLE_CATEGORY = new NonExtendableCategory().setId(6)
            .setName("Bus");
    private static final FeedbackCriteria DEFAULT_DB_FEEDBACK_CRITERIA = new FeedbackCriteria().setId(4)
            .setType(FeedbackCriteria.FeedbackType.RATING);
    private static final Transit DEFAULT_DB_TRANSIT = new Transit().setId(21).setName("1A")
            .setCategory((NonExtendableCategory) DEFAULT_DB_NON_EXTENDABLE_CATEGORY);
    private static final User DEFAULT_DB_USER = new User().setId(5);

    @Autowired
    private FeedbackRepository feedbackRepository;

    private static Feedback getTestFeedback() {
        return new Feedback().setAnswer("[{\"answer\\\" : 2, \"weight\" :1}]")
                .setFeedbackCriteria(DEFAULT_DB_FEEDBACK_CRITERIA).setTransit(DEFAULT_DB_TRANSIT)
                .setUser(DEFAULT_DB_USER).setDate(LocalDateTime.now());
    }

    @Test
    public void findByTransitIdTest() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback());
        List<Feedback> feedbackList = feedbackRepository.findByTransitId(DEFAULT_DB_TRANSIT.getId());

        assertNotNull(expectedFeedback);
        assertNotEquals(feedbackList.size(), 0);

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getTransit(), actualFeedback.getTransit());
    }

    @Test
    public void findByUserIdTest() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback());
        List<Feedback> feedbackList = feedbackRepository.findByUserIdOrderByDateDesc(DEFAULT_DB_USER.getId());

        assertNotNull(expectedFeedback);
        assertNotEquals(0, feedbackList.size());

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getUser().getId(), actualFeedback.getUser().getId());
    }

    @Test
    public void findByCriteriaIdTest() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback());
        List<Feedback> feedbackList = feedbackRepository.findByFeedbackCriteriaId(DEFAULT_DB_FEEDBACK_CRITERIA.getId());

        assertNotNull(expectedFeedback);
        assertNotEquals(feedbackList.size(), 0);

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getFeedbackCriteria(), actualFeedback.getFeedbackCriteria());
    }

    @Test
    public void findByTransitIdAndFeedbackCriteriaTypeTest() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback());
        List<Feedback> feedbackList = feedbackRepository.findByTransitIdAndFeedbackCriteriaType(
                DEFAULT_DB_TRANSIT.getId(), DEFAULT_DB_FEEDBACK_CRITERIA.getType());

        assertNotNull(expectedFeedback);
        assertNotEquals(0, feedbackList.size());

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getTransit(), actualFeedback.getTransit());
        assertEquals(expectedFeedback.getFeedbackCriteria(), actualFeedback.getFeedbackCriteria());
    }

    @Test
    public void findByTransitCategoryIdAndFeedbackCriteriaType() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback());
        List<Feedback> feedbackList = feedbackRepository.findByTransitCategoryIdAndFeedbackCriteriaType(
                DEFAULT_DB_TRANSIT.getCategory().getId(), DEFAULT_DB_FEEDBACK_CRITERIA.getType());
        assertNotNull(expectedFeedback);
        assertNotEquals(feedbackList.size(), 0);

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getFeedbackCriteria(), actualFeedback.getFeedbackCriteria());
        assertEquals(expectedFeedback.getTransit().getCategory(), actualFeedback.getTransit().getCategory());
    }

    @Test
    public void findByTransitIdAndFeedbackCriteriaTypeAndUserIdTest() {
        Feedback expectedFeedback = feedbackRepository.save(getTestFeedback().setTransit(new Transit().setId(21)));
        List<Feedback> feedbackList = feedbackRepository.findByTransitIdAndFeedbackCriteriaTypeAndUserId(
                DEFAULT_DB_TRANSIT.getId(), DEFAULT_DB_FEEDBACK_CRITERIA.getType(), DEFAULT_DB_USER.getId());
        assertNotNull(expectedFeedback);
        assertNotEquals(feedbackList.size(), 0);

        Feedback actualFeedback = feedbackList.get(0);
        assertEquals(expectedFeedback, actualFeedback);
        assertEquals(expectedFeedback.getTransit(), actualFeedback.getTransit());
        assertEquals(expectedFeedback.getUser(), actualFeedback.getUser());
        assertEquals(expectedFeedback.getFeedbackCriteria().getType(), actualFeedback.getFeedbackCriteria().getType());
    }

}
