package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.payment.CheckPaymentStatusUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.CreatePaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.ExpirePaymentUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.GetPaymentByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.ListPaymentsByOrderUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsFailedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsPaidUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.payment.status.MarkPaymentAsRefundedUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.payment.CheckPaymentStatusUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ExpirePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsRefundedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.PaymentEventPublisherPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBeanConfig {

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort,
            ExternalPaymentGatewayPort externalPaymentGatewayPort,
            MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase,
            MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase,
            PaymentEventPublisherPort paymentEventPublisherPort,
            @Value("${payment.reprocess.base-delay-ms:10000}") long baseDelayMs
    ){

        return new CreatePaymentUseCaseImpl(
                paymentRepositoryPort,
                orderRepositoryPort,
                externalPaymentGatewayPort,
                markPaymentAsPaidUseCase,
                markPaymentAsFailedUseCase,
                paymentEventPublisherPort,
                baseDelayMs
        );
    }

    @Bean
    public CheckPaymentStatusUseCase checkPaymentStatusUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            ExternalPaymentGatewayPort externalPaymentGatewayPort,
            MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase,
            MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase,
            PaymentEventPublisherPort paymentEventPublisherPort,
            @Value("${payment.reprocess.base-delay-ms:10000}") long baseDelayMs,
            @Value("${payment.reprocess.max-attempts:6}") int maxAttempts
    ){

        return new CheckPaymentStatusUseCaseImpl(
                paymentRepositoryPort,
                externalPaymentGatewayPort,
                markPaymentAsPaidUseCase,
                markPaymentAsFailedUseCase,
                paymentEventPublisherPort,
                baseDelayMs,
                maxAttempts
        );
    }

    @Bean
    public ExpirePaymentUseCase expirePaymentUseCase(
            PaymentRepositoryPort paymentRepositoryPort,
            OrderRepositoryPort orderRepositoryPort
    ){

        return new ExpirePaymentUseCaseImpl(paymentRepositoryPort, orderRepositoryPort);
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
}
