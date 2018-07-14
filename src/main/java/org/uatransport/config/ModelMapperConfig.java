package org.uatransport.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uatransport.config.modelmapperconfig.CommentMapper;
import org.uatransport.config.modelmapperconfig.FeedbackMapper;
import org.uatransport.config.modelmapperconfig.TransitMapper;
import org.uatransport.config.modelmapperconfig.UserInfoMapper;
import org.uatransport.entity.Comment;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.Transit;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.CommentDTO;
import org.uatransport.entity.dto.FeedbackDTO;
import org.uatransport.entity.dto.TransitDTO;
import org.uatransport.entity.dto.UserInfo;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Transit.class, TransitDTO.class).setConverter(new TransitMapper());
        modelMapper.createTypeMap(Comment.class, CommentDTO.class).setConverter(new CommentMapper());
        modelMapper.createTypeMap(Feedback.class, FeedbackDTO.class).setConverter(new FeedbackMapper());
        modelMapper.createTypeMap(User.class, UserInfo.class).setConverter(new UserInfoMapper());
        return modelMapper;
    }
}
