package br.com.fiap.techchallenge.core.domain.exception.order;

public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }

}
