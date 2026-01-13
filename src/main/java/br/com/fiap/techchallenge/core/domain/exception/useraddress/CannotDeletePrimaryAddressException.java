package br.com.fiap.techchallenge.core.domain.exception.useraddress;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

import java.util.List;

public class CannotDeletePrimaryAddressException extends BusinessException {

    private CannotDeletePrimaryAddressException(String message) {
        super(message);
    }

    public static CannotDeletePrimaryAddressException forUserAddress(
            String userAddressId,
            List<String> userIds) {

        return new CannotDeletePrimaryAddressException(
                "It is not allowed to delete the UserAddress (link) " + userAddressId +
                " because it is set as the primary address for the following users: " +
                 String.join(", ", userIds)
        );
    }


    public static CannotDeletePrimaryAddressException forUser(String userId) {
        return new CannotDeletePrimaryAddressException(
                "It is not allowed to remove the primary address of the user " +
                userId + " without setting another address as primary."
        );
    }


    public static CannotDeletePrimaryAddressException forAddress(String addressId, List<String> userIds) {
        return new CannotDeletePrimaryAddressException(
                "It is not allowed to delete the address " + addressId +
                " because it is set as the primary address for the following users: " +
                String.join(", ", userIds)
        );
    }
}
