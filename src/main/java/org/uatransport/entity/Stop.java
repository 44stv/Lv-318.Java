package org.uatransport.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("STOP")
@Accessors(chain = true)
public class Stop extends Point {

    private String street;
}
