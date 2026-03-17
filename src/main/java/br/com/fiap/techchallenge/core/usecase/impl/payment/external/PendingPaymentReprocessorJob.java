package br.com.fiap.techchallenge.core.usecase.impl.payment.external;

import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ReprocessPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PendingPaymentReprocessorJob {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final ReprocessPendingPaymentUseCase reprocessPendingPaymentUseCase;


    public PendingPaymentReprocessorJob(PaymentRepositoryPort paymentRepositoryPort, ReprocessPendingPaymentUseCase reprocessPendingPaymentUseCase) {
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.reprocessPendingPaymentUseCase = reprocessPendingPaymentUseCase;
    }


    @Scheduled(fixedDelayString = "${payment.reprocessing.fixed-delay-ms:30000}")
    public void reprocessPendingPayment(){

        List<Payment> pendingPayments = paymentRepositoryPort.findPendingPaymentsForReprocessing();

        for(Payment payment : pendingPayments){
            try {
                reprocessPendingPaymentUseCase.execute(payment.getId());
            }catch (Exception ex){
                System.err.println("Failed to reprocess payment " + payment.getId() + ": " + ex.getMessage());
            }
        }
    }
}
