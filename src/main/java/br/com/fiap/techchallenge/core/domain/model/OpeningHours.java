package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.exception.openinghours.InvalidOpeningHoursException;

public class OpeningHours {
    private final String opens; // e.g., "08:00"
    private final String closes; // e.g., "22:00"

    public OpeningHours(String opens, String closes) {
        if (opens == null || opens.isBlank()){
            throw new InvalidOpeningHoursException("opens must not be blank");
        }

        if (closes == null || closes.isBlank()){
            throw new InvalidOpeningHoursException("closes must not be blank");
        }

        this.opens = opens;
        this.closes = closes;
    }

    public String getOpens() {
        return opens;
    }

    public String getCloses() {
        return closes;
    }

    public String toString() {
        return "OpeningHours{" +
                "opens='" + opens + '\'' +
                ", closes='" + closes + '\'' +
                '}';
    }
}
