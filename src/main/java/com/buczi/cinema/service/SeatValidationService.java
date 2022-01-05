package com.buczi.cinema.service;

import com.buczi.cinema.model.entity.CinemaEvent;
import com.buczi.cinema.model.entity.ReservedSeat;
import com.buczi.cinema.model.entity.Seat;
import com.buczi.cinema.model.protocol.ReservationBody;
import com.buczi.cinema.model.repository.ReservationRepository;
import com.buczi.cinema.model.repository.ReservedSeatRepository;
import com.buczi.cinema.model.repository.SeatRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeatValidationService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    public boolean areSeatsChosenCorrectly(final ReservationBody reservationBody, final CinemaEvent event)
    {
        final var pickedSeats = reservationBody.getReservedSeats().stream().map(Pair::getFirst).collect(Collectors.toList());
        final var reservedSeatIds = getReservedSeats(event);

        return areAllPickedSeatsInEventHall(pickedSeats,event) &&
                arePickedSeatsFree(pickedSeats,reservedSeatIds) &&
                checkIfNoEmptySeatWasLeftBetween(pickedSeats,reservedSeatIds);
    }

    private boolean areAllPickedSeatsInEventHall(final List<Long> pickedSeats, final CinemaEvent event)
    {
        final var hall = event.getHall();
        final var seats = seatRepository.findAllIdsByHall(hall.getHallId());
        return seats.containsAll(pickedSeats);
    }

    private boolean arePickedSeatsFree(final List<Long> pickedSeats, final List<ReservedSeat> reservedSeats)
    {
        return reservedSeats.stream()
                .mapToLong(reservedSeat -> reservedSeat.getReservationId().getSeat().getSeatId())
                .noneMatch(pickedSeats::contains);
    }

    private boolean checkIfNoEmptySeatWasLeftBetween(final List<Long> pickedSeats, final List<ReservedSeat> reservedSeats)
    {
        final var seats = seatRepository.findAllBySeatIdIn(pickedSeats);
        final var seatsPreviouslyReserved = reservedSeats.stream()
                .map(reservedSeat -> reservedSeat.getReservationId().getSeat())
                .collect(Collectors.toList());

        final var mappedSeats = mapSeatsToRows(seats);
        final var mappedReservedSeats = mapSeatsToRows(seatsPreviouslyReserved);

        if (!checkIfCurrentlyPickedSeatsAreValid(mappedSeats))
        {
            return false;
        }

        return !checkIfBetweenReservedSeatsAndPickedSeatsAreLeftSingularSeats(mappedSeats, mappedReservedSeats);
    }

    private List<ReservedSeat> getReservedSeats(final CinemaEvent event)
    {
        final var reservationIds = reservationRepository.findAllReservations(event.getEventId());
        return reservedSeatRepository.findAllReservedSeatsByReservations(reservationIds);
    }

    private static Map<Integer,List<Integer>> mapSeatsToRows(final List<Seat> seats)
    {
        return seats.stream()
                .collect(Collectors.groupingBy(Seat::getSeatRow,
                        Collectors.mapping(Seat::getSeatNumber,
                                Collectors.toList())));
    }

    private boolean checkIfCurrentlyPickedSeatsAreValid(final Map<Integer, List<Integer>> mappedSeats)
    {
        for(final var row : mappedSeats.values())
        {
            final var sortedRow = row.stream().sorted().collect(Collectors.toList());
            for(var i = 0; i < sortedRow.size() - 1; i++)
            {
                if(sortedRow.get(i + 1) - sortedRow.get(i)  == 2)
                {
                    log.warn("Picked seats are invalid due to one seat left between them");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfBetweenReservedSeatsAndPickedSeatsAreLeftSingularSeats(final Map<Integer, List<Integer>> mappedSeats, final Map<Integer, List<Integer>> mappedReservedSeats)
    {
        for(final var rowNumber : mappedSeats.keySet())
        {
            final var addedSeats = mappedSeats.get(rowNumber);
            final var alreadyReservedSeats = mappedReservedSeats.getOrDefault(rowNumber, Collections.emptyList());

            if(alreadyReservedSeats.isEmpty())
            {
                continue;
            }

            if (!checkIfPickedSeatsAreCorrectlyPlaced(addedSeats, alreadyReservedSeats))
            {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfPickedSeatsAreCorrectlyPlaced(final List<Integer> addedSeats, final List<Integer> alreadyReservedSeats)
    {
        for(final var addedSeat : addedSeats)
        {
            for(final var reservedSeat : alreadyReservedSeats)
            {
                if(Math.abs(addedSeat - reservedSeat) == 2)
                {
                    final var smallerSeat = Math.min(addedSeat,reservedSeat);
                    if (!checkIfSeatBetweenSeatsIsNotLeft(addedSeats, alreadyReservedSeats, smallerSeat))
                    {
                        log.warn("Picked seats leave one place between them and already picked seats");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkIfSeatBetweenSeatsIsNotLeft(final List<Integer> addedSeats, final List<Integer> alreadyReservedSeats, final int smallerSeat)
    {
        return addedSeats.contains(smallerSeat + 1) || alreadyReservedSeats.contains(smallerSeat + 1);
    }
}
