package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import java.util.List;

public interface ListOrdersByUserUseCase {

    List<Order> execute(String clientId);

}