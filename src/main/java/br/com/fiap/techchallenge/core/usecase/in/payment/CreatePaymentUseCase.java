package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.usecase.in.payment.dto.CreatePaymentCommand;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;

public interface CreatePaymentUseCase {

    PaymentView execute(CreatePaymentCommand command);
}
