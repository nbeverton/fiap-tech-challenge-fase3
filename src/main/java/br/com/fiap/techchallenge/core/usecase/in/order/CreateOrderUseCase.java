package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.dto.CreateOrderCommand;

public interface CreateOrderUseCase {
    Order execute(CreateOrderCommand command);
}
