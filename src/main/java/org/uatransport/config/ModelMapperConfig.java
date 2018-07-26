package org.uatransport.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uatransport.config.modelmapperconfig.*;
import org.uatransport.entity.*;
import org.uatransport.entity.dto.*;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Transit.class, TransitDTO.class).setConverter(new TransitMapper());
        modelMapper.createTypeMap(Comment.class, CommentDTO.class).setConverter(new CommentMapper());
        modelMapper.createTypeMap(CommentRating.class, CommentRatingDTO.class).setConverter(new CommentRatingMapper());
        modelMapper.createTypeMap(Feedback.class, FeedbackDTO.class).setConverter(new FeedbackMapper());
        modelMapper.createTypeMap(User.class, UserInfo.class).setConverter(new UserInfoMapper());
        return modelMapper;
    }
}
