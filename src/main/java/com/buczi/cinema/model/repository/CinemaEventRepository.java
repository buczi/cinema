package com.buczi.cinema.model.repository;

import com.buczi.cinema.model.entity.CinemaEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaEventRepository extends CrudRepository<CinemaEvent,Long> {
    @Query("from CinemaEvent where eventTime between :startInterval and :endInterval order by movie.name desc, eventTime")
    List<CinemaEvent> findAllEventsInGivenPeriodOrderedByMovieNameAndTime(final @Param("startInterval")long startInterval, final @Param("endInterval")long endInterval);
}
