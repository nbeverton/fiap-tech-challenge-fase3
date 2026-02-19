package br.com.fiap.techchallenge.core.usecase.in.payment.status;

public interface MarkPaymentAsRefundedUseCase {

    void execute(String orderId, String paymentId);
}
