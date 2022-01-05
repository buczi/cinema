package com.buczi.cinema.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Validated
@Table(name = "Reservation")
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;

    // Name of a client making the reservation
    @Size(min=3)
    private String name;

    // Surname of a client making the reservation
    @Size(min=3)
    private String surname;

    // Time when the reservation was submitted in epoch milliseconds
    @NotNull
    private long submitDate;

    // State of reservation
    private boolean finished;

    // Event to which reservation applies
    @OneToOne(targetEntity = CinemaEvent.class, fetch = FetchType.LAZY)
    private CinemaEvent event;
}
