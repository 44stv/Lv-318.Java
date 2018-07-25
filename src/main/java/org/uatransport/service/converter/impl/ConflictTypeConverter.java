package org.uatransport.service.converter.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.uatransport.entity.Feedback;
import org.uatransport.service.converter.ConversionStrategy;
import org.uatransport.service.converter.model.ConflictFeedback;

import java.util.List;

public class ConflictTypeConverter implements ConversionStrategy<Double> {

    @Override
    @SneakyThrows
    public Double convert(Feedback feedback) {
        List<ConflictFeedback> answers = new ObjectMapper().readValue(feedback.getAnswer(),
                new TypeReference<List<ConflictFeedback>>() {
                });

        return calculateRating(answers);
    }

    private Double calculateRating(List<ConflictFeedback> answers) {
        return answers.stream().mapToInt(ConflictFeedback::getRate).average().orElse(0.0);
    }
}
