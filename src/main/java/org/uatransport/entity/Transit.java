package org.uatransport.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class Transit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "transit")
    private List<Feedback> feedbacks;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private NonExtendableCategory category;

    @ManyToMany
    @JoinTable(name = "transit_point", joinColumns = { @JoinColumn(name = "transit_id") }, inverseJoinColumns = {
            @JoinColumn(name = "point_id") })
    @OrderColumn(name = "point_index")
    private List<Point> points;

}
