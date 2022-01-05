package com.buczi.cinema.model.repository;

import com.buczi.cinema.model.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation,Long> {
    @Query("select reservationId from Reservation where event.eventId = :eventId")
    List<Long> findAllReservations(final @Param("eventId") long eventId);
}
