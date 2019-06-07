package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

}
