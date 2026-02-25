package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.order.status.*;
import br.com.fiap.techchallenge.core.usecase.impl.order.CreateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.GetOrderByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.ListOrdersByClientUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.ListOrdersUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.UpdateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.order.status.*;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.GetOrderByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersByClientUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            UserAddressRepositoryPort userAddressRepositoryPort,
            AddressRepositoryPort addressRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {
        return new CreateOrderUseCaseImpl(
                orderRepositoryPort,
                userAddressRepositoryPort,
                addressRepositoryPort,
                restaurantRepositoryPort);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort, RestaurantRepositoryPort restaurantRepositoryPort) {
        return new GetOrderByIdUseCaseImpl(orderRepositoryPort, restaurantRepositoryPort);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepositoryPort orderRepositoryPort, RestaurantRepositoryPort restaurantRepositoryPort) {
        return new ListOrdersUseCaseImpl(orderRepositoryPort, restaurantRepositoryPort);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {
        return new UpdateOrderUseCaseImpl(orderRepositoryPort, restaurantRepositoryPort);
    }

    @Bean
    public MarkOrderAsAcceptedUseCase acceptOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {

        return new br.com.fiap.techchallenge.core.usecase.impl.order.status.MarkOrderAsAcceptedUseCaseImpl(
                orderRepositoryPort,
                restaurantRepositoryPort
        );
    }

    @Bean
    public MarkOrderAsDeliveredUseCase deliverOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {

        return new MarkOrderAsDeliveredUseCaseImpl(
                orderRepositoryPort,
                restaurantRepositoryPort
        );
    }

    @Bean
    public MarkOrderAsOutForDeliveryUseCase outForDeliveryOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {

        return new MarkOrderAsOutForDeliveryUseCaseImpl(
                orderRepositoryPort,
                restaurantRepositoryPort
        );
    }

    @Bean
    public MarkOrderAsRejectedUseCase rejectOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            PaymentRepositoryPort paymentRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {

        return new MarkOrderAsRejectedUseCaseImpl(
                orderRepositoryPort,
                paymentRepositoryPort,
                restaurantRepositoryPort);
    }

    @Bean
    public MarkOrderAsPreparingUseCase startPreparingOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort) {

        return new MarkOrderAsPreparingUseCaseImpl(
                orderRepositoryPort,
                restaurantRepositoryPort);
    }

    @Bean
    public ListOrdersByClientUseCase listOrdersByClientUseCase(
            OrderRepositoryPort orderRepositoryPort) {
        return new ListOrdersByClientUseCaseImpl(orderRepositoryPort);
    }
}