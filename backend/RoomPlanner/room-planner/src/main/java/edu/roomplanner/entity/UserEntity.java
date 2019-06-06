package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Setter
@Getter
public class UserEntity {

    @Id
    @SequenceGenerator(name = "gen_user_id", allocationSize = 1, sequenceName = "seq_user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_id")
    private Long id;

    @Column(name = "birth_date")
    private Date birthDate;
}
