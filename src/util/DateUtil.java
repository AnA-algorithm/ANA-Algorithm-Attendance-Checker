package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public class DateUtil {

    public static LocalDateTime parseIntToLocalDateTime(final int time, final ZoneId zoneId) {
        final Instant commitInstant = Instant.ofEpochSecond(time);
        final ZonedDateTime authorDateTime = ZonedDateTime.ofInstant(commitInstant, zoneId);
        return authorDateTime.toLocalDateTime();
    }
}
