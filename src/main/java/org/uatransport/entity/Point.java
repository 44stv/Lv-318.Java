package org.uatransport.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@DiscriminatorColumn(name = "type")
@Table(name = "point")
@DiscriminatorValue("POINT")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private Double lat;

    private Double lng;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "dir", updatable = false)
    private Point.DIRECTION direction;

    @RequiredArgsConstructor
    public enum DIRECTION {
        FORWARD, BACKWARD
    }
}
