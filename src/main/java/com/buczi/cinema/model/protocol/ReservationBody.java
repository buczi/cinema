package com.buczi.cinema.model.protocol;

import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Parameters that are sent to application during the seat reservation process. Parameters are validated.
 */
@Data
@Validated
public class ReservationBody {
    public static final Pair<Long, String> DEFAULT_TYPE = Pair.of(0L, "ADULT");

    // name - name of a person making reservation, it has to start with capital and have to be followed by at least 2 small letters
    @Size(min=3)
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$")
    private String name;

    // surname - surname of a person making reservation, it has to start with capital and have to be followed by at least 2 small letters,
    // moreover the surname can have two parts that are separated by dash
    @Size(min=3)
    @Pattern(regexp = "(^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$)|(^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*[-][A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*)")
    private String surname;

    // Time of submitting the request in epoch milliseconds
    @NotNull
    private long submitDate;

    // Index of an event that seats are reserved for
    @NotNull
    private long eventId;

    // Pair of reserved seat and ticket type included in reservation
    @NotNull
    @Size(min = 1)
    private List<Pair<Long,String>> reservedSeats;
}


