package com.buczi.cinema.model.protocol;

/**
 * Record used to represent data after reservation of given seats
 * reservationId - unique identifier of reservation made if 0 transaction was interrupted
 * price - total price of all reserved tickets
 * expirationTime - time in milliseconds marking the barrier after which the transaction will not be processed
 */
public record ConfirmedReservation(long reservationId,double price, long expirationTime) {
}
