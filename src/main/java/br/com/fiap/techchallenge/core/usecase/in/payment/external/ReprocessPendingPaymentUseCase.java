package br.com.fiap.techchallenge.core.usecase.in.payment.external;

public interface ReprocessPendingPaymentUseCase {

    void execute(String paymentId);
}
