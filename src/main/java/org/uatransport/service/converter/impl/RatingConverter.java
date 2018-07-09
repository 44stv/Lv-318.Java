package org.uatransport.service.converter.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.uatransport.entity.Feedback;
import org.uatransport.service.converter.ConversionStrategy;
import org.uatransport.service.converter.model.RatingFeedback;

import java.util.List;

public class RatingConverter implements ConversionStrategy<Double> {
    private static final Integer MAX_ANSWER_VALUE = 10;

    @Override
    @SneakyThrows
    public Double convert(Feedback feedback) {
        List<RatingFeedback> answers = new ObjectMapper().readValue(feedback.getAnswer(), new TypeReference<List<RatingFeedback>>() {
        });

        return calculateRating(answers);
    }

    private Double calculateMaxValue(List<RatingFeedback> answers) {
        return answers.stream()
            .mapToInt(answer -> MAX_ANSWER_VALUE * answer.getWeight())
            .average()
            .orElse(0.0);
    }

    private Double calculateValue(List<RatingFeedback> answers) {
        return answers.stream()
            .mapToInt(answer -> answer.getAnswer() * answer.getWeight())
            .average()
            .orElse(0.0);
    }

    private Double calculateRating(List<RatingFeedback> answers) {
        return (10 * calculateValue(answers)) / calculateMaxValue(answers);
    }
}
