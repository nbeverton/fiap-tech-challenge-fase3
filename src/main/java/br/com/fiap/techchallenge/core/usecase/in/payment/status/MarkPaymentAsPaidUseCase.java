package br.com.fiap.techchallenge.core.usecase.in.payment.status;

public interface MarkPaymentAsPaidUseCase {

    void execute(String orderId, String paymentId);
}
