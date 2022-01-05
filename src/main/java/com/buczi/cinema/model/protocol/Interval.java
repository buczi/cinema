package com.buczi.cinema.model.protocol;

/**
 * Representation of time interval
 * startTime - start of an event in milliseconds
 * endTime - end of an event in milliseconds
 */
public record Interval(long startTime, long endTime) {
    public boolean isValid()
    {
        return startTime < endTime;
    }
}
