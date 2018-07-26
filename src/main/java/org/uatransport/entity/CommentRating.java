package org.uatransport.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "comment_ratings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"}))
public class CommentRating {

    public static final short LIKE_VALUE = 1;
    public static final short DISLIKE_VALUE = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private short value;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
