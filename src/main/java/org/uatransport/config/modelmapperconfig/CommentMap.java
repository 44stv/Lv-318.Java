package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Comment;
import org.uatransport.entity.dto.CommentDTO;

@Component
@RequiredArgsConstructor
public class CommentMap implements Converter<Comment, CommentDTO> {

    @Override
    public CommentDTO convert(MappingContext<Comment, CommentDTO> mappingContext) {
        Comment source = mappingContext.getSource();
        CommentDTO destination = mappingContext.getDestination();

        destination.setId(source.getId());
        destination.setCommentText(source.getCommentText());
        destination.setPostDate(source.getCreatedDate());
        destination.setModifiedDate(source.getModifiedDate());
        destination.setUserId(source.getUser().getId());
        destination.setTransitId(source.getTransit().getId());
//        destination.setParentCommentId(source.getParentComment().getId());
        destination.setChildrenComments(source.getChildrenComments());

        return destination;
    }
}

///asdasdasdsa
//    /asdsadasdsa
//    ...(show more)
//
///asdasdasdsa
//    /asdsadasdsa
//        /asdasdsa
//        /asdasdasd
//    /dasdasdasdas
//
///asdasdasdasd
