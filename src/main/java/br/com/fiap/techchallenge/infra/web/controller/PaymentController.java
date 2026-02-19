package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.infra.web.dto.payment.CreatePaymentRequest;
import br.com.fiap.techchallenge.infra.web.dto.payment.PaymentResponse;
import br.com.fiap.techchallenge.infra.web.mapper.payment.PaymentRequestMapper;
import br.com.fiap.techchallenge.infra.web.mapper.payment.PaymentResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final GetPaymentByIdUseCase getPaymentByIdUseCase;
    private final ListPaymentsByOrderUseCase listPaymentsByOrderUseCase;

    private final MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;
    private final MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase;

    public PaymentController(CreatePaymentUseCase createPaymentUseCase, GetPaymentByIdUseCase getPaymentByIdUseCase, ListPaymentsByOrderUseCase listPaymentsByOrderUseCase, MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase, MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.getPaymentByIdUseCase = getPaymentByIdUseCase;
        this.listPaymentsByOrderUseCase = listPaymentsByOrderUseCase;
        this.markPaymentAsPaidUseCase = markPaymentAsPaidUseCase;
        this.markPaymentAsFailedUseCase = markPaymentAsFailedUseCase;
    }


    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @PathVariable String orderId,
            @RequestBody CreatePaymentRequest request
    ) {

        PaymentResponse response = PaymentResponseMapper.toResponse(

                createPaymentUseCase.execute(PaymentRequestMapper.toCommand(orderId, request))

        );

        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable String orderId,
            @PathVariable String paymentId
    ) {

        PaymentResponse response = PaymentResponseMapper.toResponse(

                getPaymentByIdUseCase.execute(orderId, paymentId)

        );

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<PaymentResponse>> listPayments(
            @PathVariable String orderId
    ) {

        List<PaymentResponse> responses =

                listPaymentsByOrderUseCase.execute(orderId)
                        .stream()
                        .map(PaymentResponseMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }


    @PatchMapping("/{paymentId}/paid")
    public ResponseEntity<Void> markAsPaid(
            @PathVariable String orderId,
            @PathVariable String paymentId
    ){
        markPaymentAsPaidUseCase.execute(orderId, paymentId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{paymentId}/failed")
    public ResponseEntity<Void> markAsFailed(
            @PathVariable String orderId,
            @PathVariable String paymentId
    ){
        markPaymentAsFailedUseCase.execute(orderId,paymentId);
        return ResponseEntity.noContent().build();
    }
}
