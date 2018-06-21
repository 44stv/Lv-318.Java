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
    @JoinTable(name = "transit_stop", joinColumns = { @JoinColumn(name = "transit_id") }, inverseJoinColumns = {
            @JoinColumn(name = "stop_id") })
    @OrderColumn(name = "stop_index")
    private List<Point> points;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", updatable = false)
    private Transit.DIRECTION direction;

    @RequiredArgsConstructor
    public enum DIRECTION {
      FORWARD,
      BACKWARD
    }
}
