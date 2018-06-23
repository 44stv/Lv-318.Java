package org.uatransport.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "feedback_criteria")
@EqualsAndHashCode(of = "id")
public class FeedbackCriteria {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer weight;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "criteria_id")
    private List<Question> questions;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", updatable = false)
    private FeedbackType type;

    @RequiredArgsConstructor
    public enum FeedbackType {
        RATING, ROUTE_CAPACITY, ACCEPTER, HOURS_CAPACITY
    }
}
