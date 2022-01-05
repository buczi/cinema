package com.buczi.cinema.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Utility class providing time translation to milliseconds
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MillisecondsProvider {

    public static long nowToMilliseconds()
    {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public static long minutesToMilliseconds(final long minutes)
    {
        return minutes * 60 * 1000;
    }

}
