package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;

public interface GetPaymentByIdUseCase {

    PaymentView execute(String orderId, String paymentId);
}
