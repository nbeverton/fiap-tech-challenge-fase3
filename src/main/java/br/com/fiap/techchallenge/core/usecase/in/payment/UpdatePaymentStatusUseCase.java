package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.UpdatePaymentStatusCommand;

public interface UpdatePaymentStatusUseCase {

    PaymentView execute(UpdatePaymentStatusCommand command);
}
