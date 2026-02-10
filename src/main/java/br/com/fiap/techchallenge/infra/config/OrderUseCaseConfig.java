package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.order.AcceptOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.OrderManagementUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.AcceptOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public OrderManagementUseCase orderManagementUseCase(
            OrderRepositoryPort orderRepositoryPort,
            UserAddressRepositoryPort userAddressRepositoryPort,
            AddressRepositoryPort addressRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort
    ) {
        return new OrderManagementUseCase(
                orderRepositoryPort,
                userAddressRepositoryPort,
                addressRepositoryPort,
                restaurantRepositoryPort
        );
    }


    @Bean
    public AcceptOrderUseCase acceptOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new AcceptOrderUseCaseImpl(orderRepositoryPort);
    }
}
