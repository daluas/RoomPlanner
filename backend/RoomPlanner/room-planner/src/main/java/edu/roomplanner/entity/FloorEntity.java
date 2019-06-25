package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "floors")
@Getter
@Setter
public class FloorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_floor_id")
    @SequenceGenerator(name = "seq_floor_id", sequenceName = "seq_floor_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @OneToMany(mappedBy = "floor")
    private Set<RoomEntity> rooms;
}
