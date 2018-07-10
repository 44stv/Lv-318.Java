package org.uatransport.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "question")
@EqualsAndHashCode(of = "id")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer weight;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", updatable = false)

    private QuestionType type;

    @RequiredArgsConstructor
    public enum QuestionType {
        SIMPLE, STOP, TIME, PERCENTAGE
    }

}
