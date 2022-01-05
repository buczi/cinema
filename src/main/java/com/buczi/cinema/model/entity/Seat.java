package com.buczi.cinema.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Validated
@Table(name = "Seat")
@NoArgsConstructor
@Data
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatId;

    // Row in which seat is placed
    @Min(1)
    private int seatRow;

    // Number in row which represents seat position
    @Min(1)
    private int seatNumber;

    // Hall in which the seat is placed
    @ManyToOne(targetEntity = CinemaHall.class, fetch = FetchType.LAZY)
    private CinemaHall hall;
}
