package com.buczi.cinema.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Representation of movie showing
 */
@Entity
@Validated
@Table(name = "Event")
@NoArgsConstructor
@Data
public class CinemaEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    // Time when certain movie is taking place, cannot be smaller than year 2000
    // Date is represented as an epoch timestamp in milliseconds
    @NonNull
    @Min(946684800000L)
    private long eventTime;

    // Movie that will be shown during the event
    @NonNull
    @ManyToOne(targetEntity = Movie.class)
    private Movie movie;

    // Place in which given event will take place
    @ManyToOne(targetEntity = CinemaHall.class)
    private CinemaHall hall;

}
