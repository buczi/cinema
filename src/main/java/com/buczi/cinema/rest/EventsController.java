package com.buczi.cinema.rest;

import com.buczi.cinema.model.entity.CinemaEvent;
import com.buczi.cinema.model.protocol.Interval;
import com.buczi.cinema.model.repository.CinemaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventsController {

    private final CinemaEventRepository cinemaEventRepository;

    /**
     * Present all available screenings in given time period
     * @param interval time interval in which screening should be returned
     * @return empty list, HttpStatus.BAD_REQUEST - if interval start was after interval end
     * correct list, HttpStatus.OK - otherwise
     */
    @PostMapping("/events")
    public ResponseEntity<List<CinemaEvent>> getEventsDuringInterval(final @RequestBody Interval interval)
    {
        return interval.isValid() ? getEventsWithOKStatus(interval) : new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<List<CinemaEvent>> getEventsWithOKStatus(final Interval interval)
    {
        final var events = cinemaEventRepository
                .findAllEventsInGivenPeriodOrderedByMovieNameAndTime(interval.startTime(), interval.endTime());

        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
