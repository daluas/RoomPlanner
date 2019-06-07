package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name",unique = true,nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roleEntityList")
    private List<UserEntity> userEntityList;

    @ManyToMany
    @JoinTable(
            name = "roles_rights",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    private List<RightEntity> rightEntityList;
}
