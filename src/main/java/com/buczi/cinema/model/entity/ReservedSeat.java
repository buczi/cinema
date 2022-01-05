package com.buczi.cinema.model.entity;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Validated
@Table(name = "Reserved_Seat")
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservedSeat {

    @EmbeddedId
    private ReservationId reservationId;

    // Type of ticket that is associated with reserved seat
    @ManyToOne(targetEntity = TicketType.class)
    private TicketType ticketType;

    @Getter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationId implements Serializable
    {
        // Reservation to which the seat is connected
        @OneToOne(targetEntity = Reservation.class)
        private Reservation reservationId;

        // Seat in given hall
        @ManyToOne(targetEntity = Seat.class)
        private Seat seat;
    }
}
