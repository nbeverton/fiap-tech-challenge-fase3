package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.order.status.RejectOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

import java.util.List;
import java.util.Set;

public class RejectOrderUseCaseImpl implements RejectOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;
    private final UserRepositoryPort userRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;

    public RejectOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort, UserRepositoryPort userRepositoryPort, PaymentRepositoryPort paymentRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
        this.userRepositoryPort = userRepositoryPort;
        this.paymentRepositoryPort = paymentRepositoryPort;
    }

    @Override
    public void reject(String orderId) {

        Order order = orderFinder.findById(orderId);

        User user = userRepositoryPort.findById(order.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(order.getUserId()));

        if(user.getUserType() == UserType.CLIENT) {

            if(order.getOrderStatus() == OrderStatus.PAYMENT_CONFIRMED
                    || order.getOrderStatus() == OrderStatus.PAID
            ){

                refundAllPaymentsByOrderId(orderId);

            }else{

                requireStatus(
                        order.getOrderStatus(),
                        Set.of(OrderStatus.CREATED, OrderStatus.AWAITING_PAYMENT, OrderStatus.PAYMENT_CONFIRMED, OrderStatus.PAID),
                        "Order can only be reject when status is CREATED, AWAITING_PAYMENT, PAYMENT_CONFIRMED or PAID."
                );
            }
            order.markOrderAsCancel();
            orderRepositoryPort.save(order);
        }

        else if(user.getUserType() == UserType.OWNER){

            if(!(order.getOrderStatus() == OrderStatus.CREATED
                    || order.getOrderStatus() == OrderStatus.AWAITING_PAYMENT)){

                refundAllPaymentsByOrderId(orderId);

                order.markOrderAsCancel();
                orderRepositoryPort.save(order);

            }else{

                order.markOrderAsCancel();
                orderRepositoryPort.save(order);
            }
        }
    }


    // ============================
    // Helpers
    // ============================
    private void requireStatus(OrderStatus orderStatus, Set<OrderStatus> allowed, String messageIfInvalid) {
        if (!allowed.contains(orderStatus)) {
            throw new InvalidOrderStatusException(messageIfInvalid);
        }
    }

    private void refundAllPaymentsByOrderId(String orderId){
        List<Payment> payments = paymentRepositoryPort.findByOrderId(orderId);
        payments.forEach(Payment::markAsRefunded);

        paymentRepositoryPort.saveAll(payments);
    }
}