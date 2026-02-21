package br.com.fiap.techchallenge.core.usecase.in.order.status;

public interface MarkOrderAsRejectedUseCase {
    
    void reject(String orderId);
}
