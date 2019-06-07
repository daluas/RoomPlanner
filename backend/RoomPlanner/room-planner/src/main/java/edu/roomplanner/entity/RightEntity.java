package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rights")
@Getter
@Setter
public class RightEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name",unique = true,nullable = false)
    private String name;

    @ManyToMany(mappedBy = "rightEntityList")
    private List<RoleEntity> roleEntityList;
}
