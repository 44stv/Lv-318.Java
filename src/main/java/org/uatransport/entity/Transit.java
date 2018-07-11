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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "transit")
    private List<Feedback> feedbacks;

    @JsonManagedReference(value = "transitJson")
    @OneToMany(mappedBy = "transit")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private NonExtendableCategory category;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "transit_stop", joinColumns = { @JoinColumn(name = "transit_id") }, inverseJoinColumns = {
            @JoinColumn(name = "stop_id") })
    @OrderColumn(name = "stop_index")
    private List<Stop> stops;

}
