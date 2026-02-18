package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.model.Order;

public interface GetOrderByIdUseCase {
    Order execute(String orderId);
}
