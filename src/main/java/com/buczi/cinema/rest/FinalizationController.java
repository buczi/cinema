package com.buczi.cinema.rest;

import com.buczi.cinema.model.entity.Reservation;
import com.buczi.cinema.model.protocol.ConfirmedReservation;
import com.buczi.cinema.model.repository.ReservationRepository;
import com.buczi.cinema.model.repository.ReservedSeatRepository;
import com.buczi.cinema.time.MillisecondsProvider;
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
public class FinalizationController {

    private final ReservationRepository reservationRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    /**
     * Confirm reservation after payment or after expiration time. Adjust database entities in regard of
     * state of confirmation and expiration time.
     * @param confirmedReservation information about reservation made and status of transaction
     * @return Message regarding result of finalization
     */
    @PostMapping("/finalize")
    @ResponseStatus(HttpStatus.OK)
    public String finalizeReservation(final @RequestBody @Valid ConfirmedReservation confirmedReservation)
    {
        final var reservation = reservationRepository.findById(confirmedReservation.reservationId());
        final var reservationExpired = MillisecondsProvider.nowToMilliseconds() >= confirmedReservation.expirationTime();
        return reservation.isPresent() ? handleReservation(reservation.get(),reservationExpired) : "Given reservation does not exist";
    }

    private String handleReservation(final Reservation reservation, final boolean reservationExpired)
    {
        if(reservationExpired)
        {
            return removeExpiredReservation(reservation);
        }
        else if(reservation.isFinished())
        {
            return "Given reservation has already finished";
        }
        return saveReservation(reservation);
    }

    private String removeExpiredReservation(final Reservation reservation)
    {
        reservedSeatRepository.removeAllReservedSeatByReservationId(reservation.getReservationId());
        reservationRepository.delete(reservation);
        return "Removed reservation due to surpassing expiration date";
    }

    private String saveReservation(final Reservation reservation)
    {
        reservation.setFinished(true);
        reservationRepository.save(reservation);
        return "Successfully added reservation with Id : " + reservation.getReservationId();
    }
}
