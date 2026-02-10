package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.UpdatePaymentStatusCommand;

public interface UpdatePaymentStatusUseCase {

    PaymentStatus execute(UpdatePaymentStatusCommand command);
}
