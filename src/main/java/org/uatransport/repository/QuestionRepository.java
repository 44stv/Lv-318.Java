package org.uatransport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.uatransport.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Optional<Question> findById(Integer id);

    List<Question> findByPriority(Integer priority);

    List<Question> findByName(String questionName);

    @Query(value = "SELECT * FROM question WHERE criteria_id = ?1", nativeQuery = true)
    List<Question> findByFeedbackCriteriaId(Integer feedbackCriteriaId);

    List<Question> findByWeight(Integer weight);

    List<Question> findByType(Question.QuestionType type);
}
