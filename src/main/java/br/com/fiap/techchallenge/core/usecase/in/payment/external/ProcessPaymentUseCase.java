package br.com.fiap.techchallenge.core.usecase.in.payment.external;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;

public interface ProcessPaymentUseCase {

    void execute(Order order, Payment payment);
}
