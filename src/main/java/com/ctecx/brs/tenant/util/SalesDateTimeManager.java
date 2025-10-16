package com.ctecx.brs.tenant.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SalesDateTimeManager {
    private static final ZoneId TIMEZONE_UTC_PLUS_3 = ZoneId.of("UTC+3");
  /*  private static final LocalTime SALES_START_TIME = LocalTime.of(8, 0);*/
    private static final LocalTime SALES_START_TIME = LocalTime.of(7, 0);
    public ZonedDateTime getCurrentTransactionDateTime() {
        return ZonedDateTime.now(TIMEZONE_UTC_PLUS_3);
    }

    public LocalDate getSalesDate(ZonedDateTime transactionDateTime) {
        ZonedDateTime utcPlus3DateTime = transactionDateTime.withZoneSameInstant(TIMEZONE_UTC_PLUS_3);
        LocalDate transactionDate = utcPlus3DateTime.toLocalDate();
        LocalTime transactionTime = utcPlus3DateTime.toLocalTime();

        if (transactionTime.isBefore(SALES_START_TIME)) {
            return transactionDate.minusDays(1);
        } else {
            return transactionDate;
        }
    }

    public void logTimezoneInfo() {
        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemTime = ZonedDateTime.now(systemZone);
        ZonedDateTime utcPlus3Time = ZonedDateTime.now(TIMEZONE_UTC_PLUS_3);

        System.out.println("System Timezone: " + systemZone);
        System.out.println("Current System Time: " + systemTime);
        System.out.println("Sales Timezone (UTC+3): " + TIMEZONE_UTC_PLUS_3);
        System.out.println("Current Sales Time (UTC+3): " + utcPlus3Time);
    }
}