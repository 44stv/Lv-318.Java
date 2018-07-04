package org.uatransport.entity.dto;

import lombok.Data;
import org.uatransport.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Integer id;
    private String commentText;
    private LocalDateTime postDate;
    private LocalDateTime modifiedDate;
    private Integer userId;
    private Integer transitId;
    private Integer parentCommentId;
    private List<Comment> childrenComments;
}
