package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.entity.Question;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.FeedbackCriteriaRepository;
import org.uatransport.service.FeedbackCriteriaService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackCriteriaServiceImpl implements FeedbackCriteriaService {

    private final FeedbackCriteriaRepository feedbackCriteriaRepository;

    @Transactional
    public FeedbackCriteria save(FeedbackCriteria feedbackCriteria) {
        if (feedbackCriteria == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }
        return feedbackCriteriaRepository.save(feedbackCriteria);
    }

    @Override
    public void delete(Integer id) {
        feedbackCriteriaRepository.deleteById(id);
    }

    @Transactional
    public FeedbackCriteria update(FeedbackCriteria feedbackCriteria) {
        if (feedbackCriteria == null) {
            throw new IllegalArgumentException("Parameter should not be null");
        }

        if (feedbackCriteriaRepository.existsById(feedbackCriteria.getId())) {
            return feedbackCriteriaRepository.saveAndFlush(feedbackCriteria);
        }

        return feedbackCriteriaRepository.findById(feedbackCriteria.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("FeedbackCriteria with id '%s' not found", feedbackCriteria.getId())));
    }

    @Override
    public List<FeedbackCriteria> getAll() {
        return feedbackCriteriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackCriteria getById(Integer id) {

        return feedbackCriteriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("FeedbackCriteria with id '%s' not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByType(FeedbackCriteria.FeedbackType type) {
        return feedbackCriteriaRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByCategoryId(Integer id) {
        return feedbackCriteriaRepository.findFeedbackCriteriaByNonExtendableCategoryId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByQuestionsName(String question) {
        return feedbackCriteriaRepository.findByQuestionsName(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByQuestionsId(Integer questionId) {
        return feedbackCriteriaRepository.findByQuestionsId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByTypeAndCategoryId(Integer categoryId, String type) {
        return feedbackCriteriaRepository.findByTypeAndNonExtendableCategoryId(categoryId, type);
    }

    @Override
    public FeedbackCriteria getFeedbackCriteriaByTypeAndCategoryId(Integer categoryId, String type) {
        return feedbackCriteriaRepository.findFeedbackCriteriaByTypeAndNonExtendableCategoryId(categoryId,type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllEnumsType() {
        List<String> allEnumTypes = new ArrayList<>();
        for (FeedbackCriteria.FeedbackType feedbackType : FeedbackCriteria.FeedbackType.values()) {
            allEnumTypes.add(feedbackType.toString());
        }
        return allEnumTypes;
    }

    @Override
    public List<FeedbackCriteria> getByQuestionsWeight(Integer weight) {
        return feedbackCriteriaRepository.findByQuestionsWeight(weight);
    }

    @Override
    public List<FeedbackCriteria> getByQuestionsType(Question.QuestionType type) {
        return feedbackCriteriaRepository.findByQuestionsType(type);
    }

    @Override
    public List<FeedbackCriteria> getByQuestionsPriority(Integer priority) {
        return feedbackCriteriaRepository.findByQuestionsPriority(priority);
    }

}
