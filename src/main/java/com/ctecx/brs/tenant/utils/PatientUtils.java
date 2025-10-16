package com.ctecx.brs.tenant.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientUtils {

    // Helper method to generate serial number
    public static String generateSerialNumber(String patientNumber) {
        if (patientNumber == null || patientNumber.length() <= 2) {
            throw new IllegalArgumentException("Patient number is invalid");
        }

        // Get current date in YYYYMMDD format
        LocalDate currentDate = LocalDate.now();
        String datePart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Extract the numeric part of the patient number (excluding first two characters)
        String patientNumberPart = patientNumber.substring(2);

        // Concatenate date part and patient number part to form serial number
        return datePart + patientNumberPart;
    }
}
