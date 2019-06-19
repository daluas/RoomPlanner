package edu.roomplanner.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "reservations")
@Setter
@Getter
@EqualsAndHashCode
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservation_id")
    @SequenceGenerator(name = "seq_reservation_id", sequenceName = "seq_reservation_id", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endDate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity person;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private UserEntity room;

    public ReservationEntity startDate(Calendar startDate){
        this.startDate = startDate;
        return this;
    }
    public ReservationEntity endDate(Calendar endDate){
        this.endDate = endDate;
        return this;
    }
    public ReservationEntity person(UserEntity person){
        this.person = person;
        return this;
    }
    public ReservationEntity room(UserEntity room){
        this.room = room;
        return this;
    }



}
