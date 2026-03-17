package com.moveinsync.metro.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class QRUtil {

    private QRUtil() {
        // Prevent instantiation
    }

    public static String generateQRString(Long bookingId,
                                          Long sourceId,
                                          Long destinationId) {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        return "BOOKING-" + bookingId +
                "-SRC" + sourceId +
                "-DST" + destinationId +
                "-" + timestamp +
                "-" + randomPart;
    }
}