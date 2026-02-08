package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderResponseDTO;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderResponseMapper;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponseDTO> create(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CreateOrderRequest request) {

        var command = OrderWebMapper.toCommand(userId, request);

        Order created = createOrderUseCase.create(command);

        return ResponseEntity.ok(OrderResponseMapper.from(created));
    }

}
