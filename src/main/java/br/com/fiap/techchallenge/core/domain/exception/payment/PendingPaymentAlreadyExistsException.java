package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class PendingPaymentAlreadyExistsException extends BusinessException {

    public PendingPaymentAlreadyExistsException(){
        super("There is already a pending payment for this order awaiting reprocessing.");
    }
}
