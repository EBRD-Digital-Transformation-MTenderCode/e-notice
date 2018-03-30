package com.procurement.notice.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

    public LocalDateTime localNowUTC() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }

    public long milliNowUTC() {
        return localNowUTC().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public Date localToDate(final LocalDateTime startDate) {
        return Date.from(startDate.toInstant(ZoneOffset.UTC));
    }
}
