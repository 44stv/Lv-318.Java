package org.uatransport.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Integer id;
    private String commentText;
    private LocalDateTime postDate;
    private LocalDateTime modifiedDate;
    private Integer userId;
    private Integer transitId;
    private Integer parentCommentId;
    private boolean parent;
    private Integer level;
    private String images;
}
