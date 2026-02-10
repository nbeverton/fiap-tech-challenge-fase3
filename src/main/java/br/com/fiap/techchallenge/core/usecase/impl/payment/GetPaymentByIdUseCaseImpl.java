package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

public class GetPaymentByIdUseCaseImpl implements GetPaymentByIdUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public GetPaymentByIdUseCaseImpl(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentView execute(String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        return new PaymentView(
                payment.getId(),
                payment.getOrderId(),
                payment.getCreatedAt(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getProvider(),
                payment.getPaidAt(),
                payment.getFailedAt(),
                payment.getRefundedAt()
        );
    }
}
