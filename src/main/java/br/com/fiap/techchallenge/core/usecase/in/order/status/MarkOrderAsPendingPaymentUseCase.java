package br.com.fiap.techchallenge.core.usecase.in.order.status;

public interface MarkOrderAsPendingPaymentUseCase {

    void execute(String orderId, String paymentId);

}
