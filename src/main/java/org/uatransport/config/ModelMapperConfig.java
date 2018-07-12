package org.uatransport.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uatransport.config.modelmapperconfig.CommentMap;
import org.uatransport.config.modelmapperconfig.FeedbackMapper;
import org.uatransport.config.modelmapperconfig.TransitMap;
import org.uatransport.entity.Comment;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.CommentDTO;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.entity.dto.TransitDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Transit.class, TransitDTO.class).setConverter(new TransitMap());
        modelMapper.createTypeMap(Comment.class, CommentDTO.class).setConverter(new CommentMap());
        modelMapper.createTypeMap(Feedback.class, FeedbackDTO.class).setConverter(new FeedbackMapper());
        return modelMapper;
    }
}
