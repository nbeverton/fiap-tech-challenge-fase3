package br.com.fiap.techchallenge.core.usecase.in.payment;

import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;

import java.util.List;

public interface ListPaymentsByOrderUseCase {

    List<PaymentView> execute(String orderId);
}
