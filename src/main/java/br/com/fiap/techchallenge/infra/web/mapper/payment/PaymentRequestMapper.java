package br.com.fiap.techchallenge.infra.web.mapper.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.CreatePaymentCommand;
import br.com.fiap.techchallenge.infra.web.dto.payment.CreatePaymentRequest;

public class PaymentRequestMapper {


    public static CreatePaymentCommand toCommand(String orderId, CreatePaymentRequest request){

        return new CreatePaymentCommand(
                orderId,
                request.amount(),
                PaymentMethod.fromString(request.method())

        );
    }
}
