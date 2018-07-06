package org.uatransport.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
