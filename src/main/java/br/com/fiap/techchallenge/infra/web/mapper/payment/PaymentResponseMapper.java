package br.com.fiap.techchallenge.infra.web.mapper.payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.infra.web.dto.payment.PaymentResponse;

public class PaymentResponseMapper {


    public static PaymentResponse toResponse(PaymentView paymentView){

        return new PaymentResponse(
                paymentView.id(),
                paymentView.orderId(),
                paymentView.createdAt(),
                paymentView.amount(),
                paymentView.method().name(),
                paymentView.status().name(),
                paymentView.transactionId(),
                paymentView.provider(),
                paymentView.paidAt(),
                paymentView.failedAt(),
                paymentView.refundedAt()
        );
    }
}
