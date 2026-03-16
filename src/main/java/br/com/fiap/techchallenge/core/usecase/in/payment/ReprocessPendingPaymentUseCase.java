package br.com.fiap.techchallenge.core.usecase.in.payment;

public interface ReprocessPendingPaymentUseCase {

    void execute(String paymentId);
}
