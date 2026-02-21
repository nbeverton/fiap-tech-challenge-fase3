package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsAcceptedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.domain.model.Order;

public class MarkOrderAsAcceptedUseCaseImpl implements MarkOrderAsAcceptedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public MarkOrderAsAcceptedUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void accept(String orderId) {

        Order order = orderFinder.findById(orderId);

        if(order.getOrderStatus() == OrderStatus.CREATED){

            order.markOrderAsAwaitPayment();
            orderRepositoryPort.save(order);
        }

        else if(order.getOrderStatus() == OrderStatus.PAYMENT_CONFIRMED){

            order.markOrderAsPaid();
            orderRepositoryPort.save(order);
        }

        else {
            throw new InvalidOrderStatusException(
                    "Order can only be accepted when status is CREATED or PAYMENT_CONFIRMED."
            );
        }
    }
}
