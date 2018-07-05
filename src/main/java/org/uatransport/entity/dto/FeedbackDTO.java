package org.uatransport.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.modelmapper.ModelMapper;
import org.springframework.expression.ParseException;
import org.uatransport.entity.Feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class FeedbackDTO {

    private Integer id;
    private String answer;
    private Integer userId;
    private Integer transitId;
    private Integer criteriaId;
    private LocalDateTime date;

    ModelMapper modelMapper = new ModelMapper();

    public Feedback convertToEntity() throws ParseException {
        return modelMapper.map(this, Feedback.class);

    }

    public static List<Feedback> toEntity(List<FeedbackDTO> feedbackDTOList) {
        return feedbackDTOList.stream().map(FeedbackDTO::convertToEntity).collect(Collectors.toList());
    }

}
