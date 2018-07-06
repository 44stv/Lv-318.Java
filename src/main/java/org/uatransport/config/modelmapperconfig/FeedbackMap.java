package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.dto.FeedbackDTO;


@Component
@RequiredArgsConstructor
public class FeedbackMap implements Converter<Feedback, FeedbackDTO> {

    @Override
    public FeedbackDTO convert(MappingContext<Feedback, FeedbackDTO> mappingContext) {
        Feedback source = mappingContext.getSource();
        FeedbackDTO destination = mappingContext.getDestination();

        destination.setUserId(source.getUser().getId());
        destination.setCriteriaId(source.getFeedbackCriteria().getId());
        destination.setTransitId(source.getTransit().getId());
        destination.setAnswer(source.getAnswer());
        destination.setId(source.getId());

        return destination;
    }
}
