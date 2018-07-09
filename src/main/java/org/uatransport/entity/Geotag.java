package org.uatransport.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "geotag")
@EqualsAndHashCode(of = "id")
public class Geotag {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Double latitude;

    private Double longtitude;

}
