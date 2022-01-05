package com.buczi.cinema.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Validated
@NoArgsConstructor
@Table(name = "Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long movieId;

    // Title of a movie available in the cinema
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    // Length in minutes of a movie
    @Min(0)
    @Max(720)
    private long length;

    // Type of genre that best describes the movie
    @Size(min = 3, max = 40)
    private String genre;
}
