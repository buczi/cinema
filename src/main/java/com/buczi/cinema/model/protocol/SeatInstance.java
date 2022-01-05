package com.buczi.cinema.model.protocol;

/**
 * Representation of a seat and its state (if is already reserved for particular event or not)
 */
public record SeatInstance(long seatId, int seatRow, int seatNumber , boolean taken) {}
