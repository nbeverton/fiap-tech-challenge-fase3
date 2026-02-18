package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.order.AcceptOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.CreateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.DeleteOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.GetOrderByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.ListOrdersUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.UpdateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.order.AcceptOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.DeleteOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.GetOrderByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            UserAddressRepositoryPort userAddressRepositoryPort,
            AddressRepositoryPort addressRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort
    ) {
        return new CreateOrderUseCaseImpl(
                orderRepositoryPort,
                userAddressRepositoryPort,
                addressRepositoryPort,
                restaurantRepositoryPort
        );
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new GetOrderByIdUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new ListOrdersUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort
    ) {
        return new UpdateOrderUseCaseImpl(orderRepositoryPort, restaurantRepositoryPort);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new DeleteOrderUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public AcceptOrderUseCase acceptOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new AcceptOrderUseCaseImpl(orderRepositoryPort);
    }
}
