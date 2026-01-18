package br.com.fiap.techchallenge.core.domain.enums;

import br.com.fiap.techchallenge.core.domain.exception.restaurant.InvalidRestaurantException;
import br.com.fiap.techchallenge.core.domain.exception.user.InvalidUserException;

public enum CuisineType {
    ITALIAN,
    JAPANESE,
    BRAZILIAN,
    MEXICAN,
    CHINESE,
    INDIAN,
    AMERICAN,
    MEDITERRANEAN,
    OTHER;

    public static CuisineType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidRestaurantException("CuisineType must not be null or blank. Allowed: ITALIAN, JAPANESE, BRAZILIAN, MEXICAN, CHINESE, INDIAN, AMERICAN, MEDITERRANEAN, OTHER");
        }
        try {
            return CuisineType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRestaurantException("Invalid userType: " + value + ". Allowed:Allowed: ITALIAN, JAPANESE, BRAZILIAN, MEXICAN, CHINESE, INDIAN, AMERICAN, MEDITERRANEAN, OTHER");
        }
}

}

