package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.FeedbackCriteria;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.FeedbackCriteriaRepository;
import org.uatransport.service.FeedbackCriteriaService;

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
                String.format("This FeedbackCriteria does not found", feedbackCriteria)));
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
    public List<FeedbackCriteria> getByWeight(Integer weight) {
        return feedbackCriteriaRepository.findByWeight(weight);
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
    public List<FeedbackCriteria> getByQuestionsGroupId(Integer groupId) {
        return feedbackCriteriaRepository.findByQuestionsGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackCriteria> getByTypeAndCategoryId(Integer categoryId, String type) {
        return feedbackCriteriaRepository.findByTypeAndNonExtendableCategoryId(categoryId, type);
    }
}
