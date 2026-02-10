package br.com.fiap.techchallenge.core.usecase.in.payment.dto;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentCommand(

        String orderId,
        BigDecimal amount,
        PaymentMethod method
) {}
