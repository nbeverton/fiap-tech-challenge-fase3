package br.com.fiap.techchallenge.core.usecase.out.external_payment;

import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentRequest;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentResponse;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentStatusResult;

public interface ExternalPaymentGatewayPort {

    ExternalPaymentResponse submitPayment(ExternalPaymentRequest request);

    ExternalPaymentStatusResult getPaymentStatus(String paymentId);
}
