package com.buczi.cinema.service;

import com.buczi.cinema.model.entity.CinemaEvent;
import com.buczi.cinema.model.entity.Seat;
import com.buczi.cinema.model.repository.CinemaEventRepository;
import com.buczi.cinema.model.repository.ReservationRepository;
import com.buczi.cinema.model.repository.ReservedSeatRepository;
import com.buczi.cinema.model.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScreeningTest {

    private static final long GOOD_EVENT_ID = 1;
    private static final long BAD_EVENT_ID = -1;

    @Mock
    private CinemaEventRepository cinemaEventRepository;
    @Mock
    private ReservedSeatRepository reservedSeatRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SeatRepository seatRepository;

    private ScreeningService screeningService;

    @BeforeEach
    void init()
    {
        screeningService = new ScreeningService(cinemaEventRepository,reservedSeatRepository,reservationRepository,seatRepository);
    }

    @Test
    void givenCorrectEventId_ReturnSeats()
    {
        // given
        final var cinemaEvent = new CinemaEvent();
        cinemaEvent.setEventId(GOOD_EVENT_ID);

        final var seat = new Seat();
        seat.setSeatId(1);
        seat.setSeatRow(2);
        seat.setSeatNumber(1);

        final var seat2 = new Seat();
        seat2.setSeatId(2);
        seat2.setSeatRow(2);
        seat2.setSeatNumber(2);

        when(cinemaEventRepository.findById(GOOD_EVENT_ID)).thenReturn(Optional.of(cinemaEvent));
        when(seatRepository.findAllByHall(any())).thenReturn(List.of(seat,seat2));
        when(reservedSeatRepository.findAllReservedSeatIdsByReservations(any())).thenReturn(Collections.singletonList(2L));

        // when
        final var eventSeats = screeningService.getAllSeatsForEvent(GOOD_EVENT_ID);

        // then
        assertThat(eventSeats.size()).isEqualTo(2);
        assertThat(eventSeats.get(0).seatId()).isEqualTo(1L);
        assertThat(eventSeats.get(0).seatRow()).isEqualTo(2L);
        assertThat(eventSeats.get(0).seatNumber()).isEqualTo(1L);
        assertThat(eventSeats.get(1).seatId()).isEqualTo(2L);
        assertThat(eventSeats.get(0).taken()).isFalse();
        assertThat(eventSeats.get(1).taken()).isTrue();

    }

    @Test
    void givenIncorrectEventId_ReturnEmptyList()
    {
        // given
        when(cinemaEventRepository.findById(BAD_EVENT_ID)).thenReturn(Optional.empty());

        // when
        final var eventSeats = screeningService.getAllSeatsForEvent(BAD_EVENT_ID);

        // then
        assertThat(eventSeats).isEmpty();
    }
}
