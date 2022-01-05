package com.buczi.cinema.rest;

import com.buczi.cinema.model.protocol.ConfirmedReservation;
import com.buczi.cinema.model.protocol.ReservationBody;
import com.buczi.cinema.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Mark seats as reserved for given person.
     * @param reservationBody all information essential for making reservation {@link ReservationBody}
     * @return Information about made reservation, or in case of failure empty object (each value set to 0)
     */
    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmedReservation tryToReserveSeats(final @Valid @RequestBody ReservationBody reservationBody)
    {
        return reservationService.reserveSeats(reservationBody);
    }
}
