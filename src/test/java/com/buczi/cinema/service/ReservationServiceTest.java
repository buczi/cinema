package com.buczi.cinema.service;

import com.buczi.cinema.model.entity.CinemaEvent;
import com.buczi.cinema.model.entity.Seat;
import com.buczi.cinema.model.entity.TicketType;
import com.buczi.cinema.model.protocol.ReservationBody;
import com.buczi.cinema.model.repository.CinemaEventRepository;
import com.buczi.cinema.model.repository.TicketTypeRepository;
import com.buczi.cinema.time.MillisecondsProvider;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private static final long GOOD_EVENT_ID = 1;
    private static final long BAD_EVENT_ID = 2;

    @Mock
    private CinemaEventRepository cinemaEventRepository;
    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SeatValidationService seatValidationService;

    private ReservationService reservationService;

    @BeforeEach
    void init()
    {
        reservationService = new ReservationService(cinemaEventRepository,ticketTypeRepository,seatValidationService);
    }

    @Test
    void givenIncorrectEventId_ReturnEmptyReservation()
    {
        // given
        final var reservationBody = new ReservationBody();
        reservationBody.setEventId(BAD_EVENT_ID);

        when(cinemaEventRepository.findById(BAD_EVENT_ID)).thenReturn(Optional.empty());

        // when
        final var reservation = reservationService.reserveSeats(reservationBody);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.reservationId()).isEqualTo(0L);
        assertThat(reservation.expirationTime()).isEqualTo(0L);
        assertThat(reservation.price()).isEqualTo(0.0, Offset.offset(0.001d));
    }

    @Test
    void givenCorrectEventWhichStartsInLessThan15Minutes_ReturnEmptyReservation()
    {
        // given
        final var reservationBody = new ReservationBody();
        final var cinemaEvent = new CinemaEvent();
        reservationBody.setEventId(GOOD_EVENT_ID);
        cinemaEvent.setEventId(GOOD_EVENT_ID);
        cinemaEvent.setEventTime(MillisecondsProvider.nowToMilliseconds() + MillisecondsProvider.minutesToMilliseconds(5));

        when(cinemaEventRepository.findById(GOOD_EVENT_ID)).thenReturn(Optional.of(cinemaEvent));

        // when
        final var reservation = reservationService.reserveSeats(reservationBody);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.reservationId()).isEqualTo(0L);
        assertThat(reservation.expirationTime()).isEqualTo(0L);
        assertThat(reservation.price()).isEqualTo(0.0, Offset.offset(0.001d));
    }

    @Test
    void givenCorrectEventWhichStartsMoreThan15MinutesLaterAndWithIncorrectlyPickedSeats_ReturnEmptyReservation()
    {
        // given
        final var reservationBody = new ReservationBody();
        final var cinemaEvent = new CinemaEvent();
        reservationBody.setEventId(GOOD_EVENT_ID);
        cinemaEvent.setEventId(GOOD_EVENT_ID);
        cinemaEvent.setEventTime(MillisecondsProvider.nowToMilliseconds() + MillisecondsProvider.minutesToMilliseconds(5));

        when(cinemaEventRepository.findById(GOOD_EVENT_ID)).thenReturn(Optional.of(cinemaEvent));

        // when
        final var reservation = reservationService.reserveSeats(reservationBody);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.reservationId()).isEqualTo(0L);
        assertThat(reservation.expirationTime()).isEqualTo(0L);
        assertThat(reservation.price()).isEqualTo(0.0, Offset.offset(0.001d));
    }

    @Test
    void givenCorrectEventWhichStartsMoreThan15MinutesLaterAndWithCorrectlyPickedSeats_ReturnCorrectReservation()
    {
        // given
        final var reservationBody = new ReservationBody();
        final var cinemaEvent = new CinemaEvent();
        final var adultTicketType = new TicketType();
        final var firstSeat = new Seat();
        final var secondSeat = new Seat();

        reservationBody.setEventId(GOOD_EVENT_ID);
        reservationBody.setReservedSeats(List.of(Pair.of(1L,"ADULT"), Pair.of(2L,"ADULT")));
        cinemaEvent.setEventId(GOOD_EVENT_ID);
        cinemaEvent.setEventTime(MillisecondsProvider.nowToMilliseconds() + MillisecondsProvider.minutesToMilliseconds(60));
        adultTicketType.setType("ADULT");
        adultTicketType.setPrice(1.0f);
        firstSeat.setSeatId(1L);
        secondSeat.setSeatId(2L);

        when(cinemaEventRepository.findById(GOOD_EVENT_ID)).thenReturn(Optional.of(cinemaEvent));
        when(seatValidationService.areSeatsChosenCorrectly(any(),any())).thenReturn(true);
        when(ticketTypeRepository.findAll()).thenReturn(Collections.singletonList(adultTicketType));
        when(seatValidationService.getSeatRepository().findAllById(any())).thenReturn(List.of(firstSeat,secondSeat));
        // when
        final var reservation = reservationService.reserveSeats(reservationBody);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.expirationTime()).isGreaterThan(0L);
        assertThat(reservation.price()).isEqualTo(2.0, Offset.offset(0.001d));
    }
}
