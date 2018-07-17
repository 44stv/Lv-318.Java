package org.uatransport.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Comment {

    private static final long MAX_DELETE_TIME_MINUTES = 10;
    private static final long MAX_EDIT_TIME_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime modifiedDate;

    @JsonBackReference(value = "userJson")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference(value = "transitJson")
    @ManyToOne
    @JoinColumn(name = "transit_id")
    private Transit transit;

    private int level;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @JsonIgnore
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    @OrderBy("created_date ASC")
    private List<Comment> childrenComments;

    @Column(columnDefinition = "TEXT")
    private String images;

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

}
