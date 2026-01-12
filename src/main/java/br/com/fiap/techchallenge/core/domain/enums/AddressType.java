package br.com.fiap.techchallenge.core.domain.enums;

import br.com.fiap.techchallenge.core.domain.exception.address.InvalidAddressException;

public enum AddressType {
    HOME,
    WORK,
    OTHER;

    public static AddressType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAddressException(
                    "addressType must not be null or blank. Allowed values: HOME, WORK, OTHER"
            );
        }

        try {
            return AddressType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidAddressException(
                    "Invalid addressType: " + value + ". Allowed values: HOME, WORK, OTHER"
            );
        }
    }

}
