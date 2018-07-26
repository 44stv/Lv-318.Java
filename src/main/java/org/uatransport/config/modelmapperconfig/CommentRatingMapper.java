package org.uatransport.config.modelmapperconfig;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.uatransport.entity.CommentRating;
import org.uatransport.entity.dto.CommentRatingDTO;

public class CommentRatingMapper implements Converter<CommentRating, CommentRatingDTO> {
    @Override
    public CommentRatingDTO convert(MappingContext<CommentRating, CommentRatingDTO> context) {
        CommentRating source = context.getSource();
        CommentRatingDTO destination = context.getDestination();
        
        destination.setId(source.getId());
        destination.setUserId(source.getUser().getId());
        destination.setCommentId(source.getComment().getId());
        destination.setValue(source.getValue());

        return destination;
    }
}
