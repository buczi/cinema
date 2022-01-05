package com.buczi.cinema.service;

import com.buczi.cinema.model.entity.*;
import com.buczi.cinema.model.protocol.ConfirmedReservation;
import com.buczi.cinema.model.protocol.ReservationBody;
import com.buczi.cinema.model.repository.CinemaEventRepository;
import com.buczi.cinema.model.repository.TicketTypeRepository;
import com.buczi.cinema.time.MillisecondsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationService {

    private final CinemaEventRepository cinemaEventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    private final SeatValidationService seatValidationService;

    @Transactional
    public ConfirmedReservation reserveSeats(final ReservationBody reservationBody)
    {
        final var event = cinemaEventRepository.findById(reservationBody.getEventId());

        if(event.isPresent())
        {
            final var isQuoterToEventStart =  event.get().getEventTime() - MillisecondsProvider.nowToMilliseconds() > MillisecondsProvider.minutesToMilliseconds(15L);
            if(isQuoterToEventStart &&  seatValidationService.areSeatsChosenCorrectly(reservationBody,event.get()))
            {
                final var reservation = createReservation(reservationBody, event.get());
                final var seatReservations = createSeatReservations(reservationBody, reservation);

                seatValidationService.getReservedSeatRepository().saveAll(seatReservations);

                final var price =  seatReservations.stream().mapToDouble(seat -> seat.getTicketType().getPrice()).sum();
                return new ConfirmedReservation(reservation.getReservationId(), price, MillisecondsProvider.nowToMilliseconds() + MillisecondsProvider.minutesToMilliseconds(15L));
            }

            if(!isQuoterToEventStart)
            {
                log.warn("Time to event is smaller than 15 minutes");
            }
        }

        log.warn("Unable to make reservation for : {}",reservationBody);
        return new ConfirmedReservation(0,0.0d,0L);
    }

    private Reservation createReservation(final ReservationBody reservationBody, final CinemaEvent event)
    {
        final var reservation = Reservation.builder()
                .withEvent(event)
                .withName(reservationBody.getName())
                .withSurname(reservationBody.getSurname())
                .withSubmitDate(reservationBody.getSubmitDate())
                .withFinished(false)
                .build();

        seatValidationService.getReservationRepository().save(reservation);
        return reservation;
    }

    private ArrayList<ReservedSeat> createSeatReservations(final ReservationBody reservationBody, final Reservation reservation)
    {
        final var seatReservations = new ArrayList<ReservedSeat>();
        final var ticketTypes = (List<TicketType>) ticketTypeRepository.findAll();
        final var mappedTicketTypes = ticketTypes.stream().map(TicketType::getType).collect(Collectors.toList());
        final var ids = reservationBody.getReservedSeats().stream().map(Pair::getFirst).collect(Collectors.toList());
        final var reservedSeats = (List<Seat>) seatValidationService.getSeatRepository().findAllById(ids);

        reservedSeats.forEach(seat -> {
            final String seatType = getSeatType(reservationBody, mappedTicketTypes, seat);
            final var reservationId = new ReservedSeat.ReservationId(reservation,seat);

            seatReservations.add(createReservedSeat(reservationId, ticketTypes,seatType));
        });
        return seatReservations;
    }

    private String getSeatType(final ReservationBody reservationBody, final List<String> mappedTicketTypes, final Seat seat)
    {
        return reservationBody.getReservedSeats().stream()
                .filter(pair -> pair.getFirst() == seat.getSeatId())
                .filter(pair -> mappedTicketTypes.contains(pair.getSecond()))
                .findFirst()
                .orElse(ReservationBody.DEFAULT_TYPE).getSecond();
    }

    private ReservedSeat createReservedSeat(final ReservedSeat.ReservationId reservationId, final List<TicketType> ticketTypes, final String seatType)
    {
        final var ticketType = ticketTypes.stream().filter(type -> type.getType().equals(seatType)).findFirst().get();
        return ReservedSeat.builder()
                .withReservationId(reservationId)
                .withTicketType(ticketType)
                .build();
    }

}
