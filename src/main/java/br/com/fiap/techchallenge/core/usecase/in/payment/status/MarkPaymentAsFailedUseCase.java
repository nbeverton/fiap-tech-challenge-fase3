package br.com.fiap.techchallenge.core.usecase.in.payment.status;

public interface MarkPaymentAsFailedUseCase {

    void execute(String orderId, String paymentId);
}
