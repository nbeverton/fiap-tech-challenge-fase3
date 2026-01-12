package br.com.fiap.techchallenge.core.domain.enums;

import br.com.fiap.techchallenge.core.domain.exception.user.InvalidUserException;

public enum UserType {

    OWNER,
    CLIENT;

    public static UserType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidUserException("userType must not be null or blank. Allowed: OWNER, CLIENT");
        }
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserException("Invalid userType: " + value + ". Allowed: OWNER, CLIENT");
        }
    }

}
