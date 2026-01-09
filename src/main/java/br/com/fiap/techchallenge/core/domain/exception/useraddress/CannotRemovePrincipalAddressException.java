package br.com.fiap.techchallenge.core.domain.exception.useraddress;

public class CannotRemovePrincipalAddressException extends RuntimeException {

    public CannotRemovePrincipalAddressException(String userId) {
        super(
                "It is not allowed to remove the primary address of the user "
                + userId
                + " without setting another address as primary."
        );
    }
}
