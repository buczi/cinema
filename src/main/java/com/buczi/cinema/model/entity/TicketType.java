package com.buczi.cinema.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Validated
@Table(name = "Ticket")
public class TicketType {
    // Name of group to which type applies to
    @Id
    private String type;

    // Price of ticket
    @Min(0)
    @NotNull
    private float price;
}
