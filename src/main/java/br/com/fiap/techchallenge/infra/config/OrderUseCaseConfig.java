package br.com.fiap.techchallenge.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.fiap.techchallenge.core.usecase.in.order.AcceptOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.impl.order.AcceptOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.CreateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.OrderManagementUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public OrderManagementUseCase orderManagementUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new OrderManagementUseCase(orderRepositoryPort);
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepositoryPort orderRepositoryPort) {
        return new CreateOrderUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public AcceptOrderUseCase acceptOrderUseCase(
            OrderRepositoryPort orderRepositoryPort) {
        return new AcceptOrderUseCaseImpl(orderRepositoryPort);
    }
}
