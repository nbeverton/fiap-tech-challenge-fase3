package br.com.fiap.techchallenge.core.usecase.in.order.status;

public interface MarkOrderAsPreparingUseCase {
    
    void startPreparing(String orderId);
}
