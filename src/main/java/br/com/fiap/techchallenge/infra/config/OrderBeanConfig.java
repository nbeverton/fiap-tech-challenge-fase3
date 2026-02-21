package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.order.status.*;
import br.com.fiap.techchallenge.core.usecase.impl.order.CreateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.DeleteOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.GetOrderByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.ListOrdersUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.order.UpdateOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.order.status.*;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.DeleteOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.order.GetOrderByIdUseCase;
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

    @Bean
    public DeliverOrderUseCase deliverOrderUseCase(OrderRepositoryPort orderRepositoryPort){
        return new DeliverOrderUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public OutForDeliveryOrderUseCase outForDeliveryOrderUseCase(OrderRepositoryPort orderRepositoryPort){
        return new OutForDeliveryOrderUseCaseImpl(orderRepositoryPort);
    }

    @Bean
    public RejectOrderUseCase rejectOrderUseCase(
            OrderRepositoryPort orderRepositoryPort,
            UserRepositoryPort userRepositoryPort,
            PaymentRepositoryPort paymentRepositoryPort
    ){
        return new RejectOrderUseCaseImpl(
                orderRepositoryPort,
                userRepositoryPort,
                paymentRepositoryPort
        );
    }

    @Bean
    public StartPreparingOrderUseCase startPreparingOrderUseCase(OrderRepositoryPort orderRepositoryPort){
        return new StartPreparingOrderUseCaseImpl(orderRepositoryPort);
    }
}
