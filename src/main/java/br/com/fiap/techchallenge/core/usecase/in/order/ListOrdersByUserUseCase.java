package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ListOrdersByUserUseCase {

    List<Order> execute(Authentication authentication);

}