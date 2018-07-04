package org.uatransport.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class Comment {

    private static final long MAX_DELETE_TIME_MINUTES = 10;
    private static final long MAX_EDIT_TIME_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String commentText;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime modifiedDate;

    @JsonBackReference(value = "userJson")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference(value = "transitJson")
    @ManyToOne
    @JoinColumn(name = "transit_id")
    private Transit transit;

    //TODO: delete comment or mark deleted
    @Column(nullable = false)
    private boolean deleted = false;

    private boolean modified;

//    @JsonManagedReference(value = "parentCommentJson")
//    @JsonBackReference(value = "parentCommentJson")
//    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    @JsonProperty("parent_id")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment; // change to id

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    @OrderBy("created_date ASC")
    private List<Comment> childrenComments = new ArrayList<>();

    public int commentLevel() {
        Comment comment = this;
        int level = 0;
        // if parent comment exist
        while ((comment = comment.getParentComment()) != null) {
            level++;
        }
        return level;
    }

    public boolean canDelete() {
        return LocalDateTime.now().isBefore(maxDeleteTime());
    }

    public LocalDateTime maxDeleteTime() {
        return createdDate.plusMinutes(MAX_DELETE_TIME_MINUTES);
    }

    public boolean canEdit() {
        return LocalDateTime.now().isBefore(maxEditTime());
    }

    public LocalDateTime maxEditTime() {
        return createdDate.plusMinutes(MAX_EDIT_TIME_MINUTES);
    }

//    @Lob
//    @OneToOne
//    List<byte[]> images;
//    separate table for images

}
