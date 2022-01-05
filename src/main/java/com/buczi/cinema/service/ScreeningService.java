package com.buczi.cinema.service;

import com.buczi.cinema.model.protocol.SeatInstance;
import com.buczi.cinema.model.repository.CinemaEventRepository;
import com.buczi.cinema.model.repository.ReservationRepository;
import com.buczi.cinema.model.repository.ReservedSeatRepository;
import com.buczi.cinema.model.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScreeningService {
    private final CinemaEventRepository cinemaEventRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public List<SeatInstance> getAllSeatsForEvent(final long eventId)
    {
        final var event = cinemaEventRepository.findById(eventId);

        if(event.isPresent())
        {
            final var hall = event.get().getHall();
            final var seats = seatRepository.findAllByHall(hall);
            final var activeReservations = reservationRepository.findAllReservations(eventId);
            final var takenSeats = reservedSeatRepository.findAllReservedSeatIdsByReservations(activeReservations);

            return seats.stream()
                    .map(seat -> new SeatInstance(seat.getSeatId(),seat.getSeatRow(),seat.getSeatNumber(),takenSeats.contains(seat.getSeatId())))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
