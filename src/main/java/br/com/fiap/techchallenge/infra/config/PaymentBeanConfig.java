package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.payment.CreatePaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.GetPaymentByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.ListPaymentsByOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsFailedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsPaidUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsRefundedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsRefundedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBeanConfig {

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(PaymentRepositoryPort paymentRepositoryPort, OrderRepositoryPort orderRepositoryPort){

        return new CreatePaymentUseCaseImpl(paymentRepositoryPort, orderRepositoryPort);
    }

    @Bean
    public GetPaymentByIdUseCase getPaymentByIdUseCase(PaymentRepositoryPort paymentRepositoryPort){

        return new GetPaymentByIdUseCaseImpl(paymentRepositoryPort);
    }

    @Bean
    public ListPaymentsByOrderUseCase listPaymentsByOrderUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort
    ){

        return new ListPaymentsByOrderUseCaseImpl(paymentRepositoryPort, orderRepositoryPort, restaurantRepositoryPort);
    }

    @Bean
    public MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase(PaymentRepositoryPort paymentRepositoryPort, OrderRepositoryPort orderRepositoryPort){

        return new MarkPaymentAsPaidUseCaseImpl(paymentRepositoryPort, orderRepositoryPort);
    }

    @Bean
    public MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase(PaymentRepositoryPort paymentRepositoryPort){

        return new MarkPaymentAsFailedUseCaseImpl(paymentRepositoryPort);
    }

    @Bean
    public MarkPaymentAsRefundedUseCase markPaymentAsRefundedUseCase(PaymentRepositoryPort paymentRepositoryPort, OrderRepositoryPort orderRepositoryPort){

        return new MarkPaymentAsRefundedUseCaseImpl(paymentRepositoryPort, orderRepositoryPort);
    }
}
