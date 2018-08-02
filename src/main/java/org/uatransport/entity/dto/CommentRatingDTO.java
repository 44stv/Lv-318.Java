package org.uatransport.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentRatingDTO {
    private Long id;
    private short value;
    private Integer userId;
    private Integer commentId;
}
