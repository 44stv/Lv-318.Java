package org.uatransport.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("POINT")
public class Point {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  private Double lat;

  private Double lng;
}
