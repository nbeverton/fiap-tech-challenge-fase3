package br.com.fiap.techchallenge.core.domain.valueobjects;

import br.com.fiap.techchallenge.core.domain.exception.openinghours.InvalidOpeningHoursException;

public class OpeningHours {
    private final String opens; // e.g., "08:00"
    private final String closes; // e.g., "22:00"

    public OpeningHours(String opens, String closes) {
        this.opens = validateTime("opens", opens);
        this.closes = validateTime("closes", closes);

        // closes depois de opens
         if (!isAfter(this.opens, this.closes)) {
             throw new InvalidOpeningHoursException("closes must be after opens");
         }
    }

    private String validateTime(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidOpeningHoursException(fieldName + " must not be null or blank");
        }

        String trimmed = value.trim();

        // Formato exato "HH:mm" -> 5 caracteres
        if (trimmed.length() != 5) {
            throw new InvalidOpeningHoursException(
                    fieldName + " must be in format HH:mm (e.g., 08:00, 18:30)"
            );
        }

        // Posição 2 deve ser ':'
        if (trimmed.charAt(2) != ':') {
            throw new InvalidOpeningHoursException(
                    fieldName + " must be in format HH:mm (missing ':')"
            );
        }

        // HH e mm devem ser dígitos
        char h1 = trimmed.charAt(0);
        char h2 = trimmed.charAt(1);
        char m1 = trimmed.charAt(3);
        char m2 = trimmed.charAt(4);

        if (!isDigit(h1) || !isDigit(h2) || !isDigit(m1) || !isDigit(m2)) {
            throw new InvalidOpeningHoursException(
                    fieldName + " must contain only digits in HH and mm (e.g., 08:00)"
            );
        }

        int hour = parseTwoDigits(h1, h2);
        int minute = parseTwoDigits(m1, m2);

        if (hour < 0 || hour > 23) {
            throw new InvalidOpeningHoursException(
                    fieldName + " hour must be between 00 and 23"
            );
        }

        if (minute < 0 || minute > 59) {
            throw new InvalidOpeningHoursException(
                    fieldName + " minutes must be between 00 and 59"
            );
        }

        return trimmed;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private int parseTwoDigits(char tens, char units) {
        // ('0' -> 48 em ASCII/Unicode)
        int t = tens - '0';
        int u = units - '0';
        return t * 10 + u;
    }

    //  garantir closes > opens
    private boolean isAfter(String opens, String closes) {
        int openHour = parseTwoDigits(opens.charAt(0), opens.charAt(1));
        int openMinute = parseTwoDigits(opens.charAt(3), opens.charAt(4));

        int closeHour = parseTwoDigits(closes.charAt(0), closes.charAt(1));
        int closeMinute = parseTwoDigits(closes.charAt(3), closes.charAt(4));

        if (closeHour > openHour) return true;
        if (closeHour < openHour) return false;
        return closeMinute > openMinute;
    }

    public String getOpens() {
        return opens;
    }

    public String getCloses() {
        return closes;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "opens='" + opens + '\'' +
                ", closes='" + closes + '\'' +
                '}';
    }
}
