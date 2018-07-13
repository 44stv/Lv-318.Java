package org.uatransport.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @JsonManagedReference(value = "userJson")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;

    @Column(name = "provider")
    private String provider;

    @NotNull
    @Column(name = "role", nullable = false)
    private Role role;

}
