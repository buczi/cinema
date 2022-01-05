package com.buczi.cinema.model.repository;

import com.buczi.cinema.model.entity.ReservedSeat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ReservedSeatRepository extends CrudRepository<ReservedSeat, ReservedSeat.ReservationId> {
    @Query("select reservationId.seat.seatId from ReservedSeat where reservationId.reservationId.reservationId in (:reservationIds)")
    List<Long> findAllReservedSeatIdsByReservations(final @Param("reservationIds") List<Long> reservationIds);

    @Query("from ReservedSeat where reservationId.reservationId.reservationId in (:reservationIds)")
    List<ReservedSeat> findAllReservedSeatsByReservations(final @Param("reservationIds") List<Long> reservationIds);


    @Modifying
    @Transactional
    @Query("delete from ReservedSeat where reservationId.reservationId.reservationId = :reservationId")
    void removeAllReservedSeatByReservationId(final @Param("reservationId") Long reservationId);
}
