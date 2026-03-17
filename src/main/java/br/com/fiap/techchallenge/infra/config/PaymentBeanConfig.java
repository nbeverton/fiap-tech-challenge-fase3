package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.order.status.MarkOrderAsPendingPaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.*;
import br.com.fiap.techchallenge.core.usecase.impl.payment.external.ExternalPaymentProcessor;
import br.com.fiap.techchallenge.core.usecase.impl.payment.external.ProcessPaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.external.ReprocessPendingPaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsFailedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsPaidUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsRefundedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ReprocessPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsRefundedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBeanConfig {

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort,
            ProcessPaymentUseCase processPaymentUseCase

    ){

        return new CreatePaymentUseCaseImpl(
                paymentRepositoryPort,
                orderRepositoryPort,
                processPaymentUseCase
        );
    }

    @Bean
    public GetPaymentByIdUseCase getPaymentByIdUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort
    ){

        return new GetPaymentByIdUseCaseImpl(paymentRepositoryPort, orderRepositoryPort, restaurantRepositoryPort);
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

    @Bean
    public MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase(
            OrderRepositoryPort orderRepositoryPort,
            PaymentRepositoryPort paymentRepositoryPort
    ) {
        return new MarkOrderAsPendingPaymentUseCaseImpl(orderRepositoryPort, paymentRepositoryPort);
    }

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(
            ExternalPaymentProcessor externalPaymentProcessor,
            PaymentRepositoryPort paymentRepositoryPort,
            ExternalPaymentGatewayPort externalPaymentGatewayPort,
            MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase
    ){
        return new ProcessPaymentUseCaseImpl(externalPaymentProcessor,paymentRepositoryPort,externalPaymentGatewayPort,markPaymentAsPaidUseCase);
    }

    @Bean
    public ReprocessPendingPaymentUseCase reprocessPendingPaymentUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort,
            ProcessPaymentUseCase processPaymentUseCase
    ){
        return new ReprocessPendingPaymentUseCaseImpl(
                paymentRepositoryPort,
                orderRepositoryPort,
                processPaymentUseCase
        );
    }
}
