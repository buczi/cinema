package com.buczi.cinema.service;

import com.buczi.cinema.model.entity.CinemaEvent;
import com.buczi.cinema.model.entity.ReservedSeat;
import com.buczi.cinema.model.entity.Seat;
import com.buczi.cinema.model.protocol.ReservationBody;
import com.buczi.cinema.model.repository.ReservationRepository;
import com.buczi.cinema.model.repository.ReservedSeatRepository;
import com.buczi.cinema.model.repository.SeatRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatValidationServiceTest {
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservedSeatRepository reservedSeatRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CinemaEvent event;

    private SeatValidationService seatValidationService;

    @BeforeEach
    void init()
    {
        seatValidationService = new SeatValidationService(seatRepository,reservationRepository,reservedSeatRepository);
    }

    @Test
    void givenSeatsInDifferentHall_ReturnFalse()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(-1L, "CHILD"));
        final var reservationBody = new ReservationBody();
        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);

        when(event.getHall().getHallId()).thenReturn(1L);
        when(seatRepository.findAllIdsByHall(anyLong())).thenReturn(List.of(1L,2L,3L));

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isFalse();
    }

    @Test
    void givenSeatsInAlreadyTakenPlaces_ReturnFalse()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(2L, "CHILD"));
        final var reservationBody = new ReservationBody();
        final var reservedSeat = mock(ReservedSeat.class,Answers.RETURNS_DEEP_STUBS);
        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);


        when(reservedSeat.getReservationId().getSeat().getSeatId()).thenReturn(2L);
        when(event.getHall().getHallId()).thenReturn(1L);
        when(seatRepository.findAllIdsByHall(anyLong())).thenReturn(List.of(1L,2L,3L));
        when(reservedSeatRepository.findAllReservedSeatsByReservations(any())).thenReturn(Collections.singletonList(reservedSeat));

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isFalse();
    }

    @Test
    void givenSeatsThatAreOneApartFromEachOther_ReturnFalse()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(3L, "CHILD"));
        final var reservationBody = new ReservationBody();
        final var seat = new Seat();
        final var seat2 = new Seat();

        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);
        seat.setSeatId(1);
        seat.setSeatRow(1);
        seat.setSeatNumber(1);
        seat2.setSeatId(2);
        seat2.setSeatRow(1);
        seat2.setSeatNumber(3);

        mockRepositoryDataWithoutReservedSeats(seat, seat2);

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isFalse();
    }

    @Test
    void givenSeatsThatAreCorrectlyPicked_ReturnTrue()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(2L, "CHILD"));
        final var reservationBody = new ReservationBody();
        final var seat = new Seat();
        final var seat2 = new Seat();

        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);
        seat.setSeatId(1);
        seat.setSeatRow(1);
        seat.setSeatNumber(1);
        seat2.setSeatId(2);
        seat2.setSeatRow(1);
        seat2.setSeatNumber(2);

        mockRepositoryDataWithoutReservedSeats(seat, seat2);

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isTrue();
    }

    @Test
    void givenPickedSeatsThatAreOneApartFromAlreadyReserved_ReturnFalse()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(2L, "CHILD"));
        final var reservationBody = new ReservationBody();
        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);

        prepareFullTestData(4);

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isFalse();
    }

    @Test
    void givenPickedSeatsThatAreNoApartFromAlreadyReserved_ReturnTrue()
    {
        // given
        final var reservedSeats = List.of(Pair.of(1L, "ADULT"), Pair.of(2L, "CHILD"));
        final var reservationBody = new ReservationBody();
        reservationBody.setReservedSeats(reservedSeats);
        reservationBody.setEventId(1);

        prepareFullTestData(3);

        // when
        final var chosenCorrectly = seatValidationService.areSeatsChosenCorrectly(reservationBody,event);

        // then
        assertThat(chosenCorrectly).isTrue();
    }

    private void mockRepositoryDataWithoutReservedSeats(final Seat seat, final Seat seat2)
    {
        when(event.getHall().getHallId()).thenReturn(1L);
        when(seatRepository.findAllIdsByHall(anyLong())).thenReturn(List.of(1L, 2L, 3L));
        when(seatRepository.findAllBySeatIdIn(any())).thenReturn(List.of(seat, seat2));
        when(reservedSeatRepository.findAllReservedSeatsByReservations(any())).thenReturn(Collections.emptyList());
    }

    private void prepareFullTestData(final int alreadyReservedSeatId)
    {
        final var reservedSeat = mock(ReservedSeat.class,Answers.RETURNS_DEEP_STUBS);
        final var seat = new Seat();
        final var seat2 = new Seat();
        final var alreadyReservedSeat = new Seat();


        seat.setSeatId(1);
        seat.setSeatRow(1);
        seat.setSeatNumber(1);
        seat2.setSeatId(2);
        seat2.setSeatRow(1);
        seat2.setSeatNumber(2);
        alreadyReservedSeat.setSeatId(alreadyReservedSeatId);
        alreadyReservedSeat.setSeatRow(1);
        alreadyReservedSeat.setSeatNumber(alreadyReservedSeatId);

        when(reservedSeat.getReservationId().getSeat()).thenReturn(alreadyReservedSeat);
        when(event.getHall().getHallId()).thenReturn(1L);
        when(seatRepository.findAllIdsByHall(anyLong())).thenReturn(List.of(1L, 2L, 3L,4L,5L));
        when(seatRepository.findAllBySeatIdIn(any())).thenReturn(List.of(seat, seat2));
        when(reservedSeatRepository.findAllReservedSeatsByReservations(any())).thenReturn(Collections.singletonList(reservedSeat));
    }
}
