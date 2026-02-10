package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.model.Order;

public interface MarkAsPaidOrderUseCase {

    public void markAsPaid(String orderId);
}
