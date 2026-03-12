package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;

public interface CheckPaymentStatusUseCase {

    void execute(PaymentStatusCheckMessage message);
}
