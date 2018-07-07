package org.uatransport.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.uatransport.config.modelmapperconfig.FeedbackMap;
import org.uatransport.config.modelmapperconfig.TransitMap;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.entity.dto.TransitDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Transit.class, TransitDTO.class).setConverter(new TransitMap());
        modelMapper.createTypeMap(Feedback.class, FeedbackDTO.class).setConverter(new FeedbackMap());
        return modelMapper;
    }
}
