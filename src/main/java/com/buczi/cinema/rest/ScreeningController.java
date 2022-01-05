package com.buczi.cinema.rest;

import com.buczi.cinema.model.protocol.SeatInstance;
import com.buczi.cinema.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScreeningController {

    private final ScreeningService screeningService;

    /**
     * Find all seats with their occupation for given event
     * @param eventId index of screening event
     * @return emptySeats, HttpStatus.BAD_REQUEST - when there is no event with given id
     * seats, HttpStatus.OK - otherwise
     */
    @GetMapping("/screening")
    public ResponseEntity<List<SeatInstance>> getSeatsForScreening(final @RequestParam long eventId)
    {
        final var seats = screeningService.getAllSeatsForEvent(eventId);
        return seats.isEmpty() ? new ResponseEntity<>(seats, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(seats,HttpStatus.OK);
    }
}
