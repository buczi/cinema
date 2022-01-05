package com.buczi.cinema.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Validated
@Table(name = "Hall")
@NoArgsConstructor
@Data
public class CinemaHall {

    // Hall unique id
    @Id
    @Min(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hallId;

    // Hall name displayed in the ticket
    @Nullable
    private String name;
}
