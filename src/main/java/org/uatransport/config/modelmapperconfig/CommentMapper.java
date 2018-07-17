package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Comment;
import org.uatransport.entity.dto.CommentDTO;

@Component
@RequiredArgsConstructor
public class CommentMapper implements Converter<Comment, CommentDTO> {

    @Override
    public CommentDTO convert(MappingContext<Comment, CommentDTO> mappingContext) {
        Comment source = mappingContext.getSource();
        CommentDTO destination = mappingContext.getDestination();

        destination.setId(source.getId());
        destination.setCommentText(source.getCommentText());
        destination.setCreatedDate(source.getCreatedDate());
        destination.setModifiedDate(source.getModifiedDate());
        destination.setUserId(source.getUser().getId());
        destination.setTransitId(source.getTransit().getId());
        destination.setImages(source.getImages());
        destination.setLevel(source.getLevel());

        try {
            if (source.getChildrenComments().isEmpty()) {
                destination.setParent(false);
            } else {
                destination.setParent(true);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

        try {
            destination.setParentCommentId(source.getParentComment().getId());
        } catch (NullPointerException e) {
            e.getMessage();
        }

        return destination;
    }


}