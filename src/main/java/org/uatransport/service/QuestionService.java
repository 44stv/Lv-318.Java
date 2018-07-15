package org.uatransport.service;

import org.uatransport.entity.Question;

import java.util.List;

public interface QuestionService {

    Question save(Question question);

    Question update(Question question);

    void delete(Integer id);

    List<Question> getAll();

    Question getById(Integer id);

    List<Question> getByPriority(Integer priority);

    List<Question> getByName(String name);

    List<Question> getByFeedbackCriteriaId(Integer feedbackCriteriaId);

    List<Question> getByWeight(Integer weight);

    List<Question> getByType(Question.QuestionType type);

    List<String> getAllEnumsTypeOfQuestion();

}
