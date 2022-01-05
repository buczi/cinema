package com.buczi.cinema.model.repository;

import com.buczi.cinema.model.entity.CinemaHall;
import com.buczi.cinema.model.entity.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends CrudRepository<Seat,Long> {
    List<Seat> findAllByHall(final CinemaHall hall);
    List<Seat> findAllBySeatIdIn(final List<Long> seatId);

    @Query("select seatId from Seat where hall.hallId = :hallId")
    List<Long> findAllIdsByHall(final @Param("hallId")long hallId);
}
