package org.uaTransport.service.converter.impl;

import org.uaTransport.entity.Feedback;
import org.uaTransport.entity.RatingCriteria;
import org.uaTransport.service.converter.ConversionStrategy;

import java.util.function.BiFunction;

public class RatingConversionStrategy implements BiFunction<String, RatingCriteria, Integer>, ConversionStrategy<Integer> {

    @Override
    public Integer convert(Feedback feedback) {
        return Integer.parseInt(feedback.getAnswer()) * ((RatingCriteria) feedback.getFeedbackCriteria()).getWeight();
    }

    @Override
    public Integer apply(String answer, RatingCriteria ratingCriteria) {
        return Integer.parseInt(answer) * ratingCriteria.getWeight();
    }
}