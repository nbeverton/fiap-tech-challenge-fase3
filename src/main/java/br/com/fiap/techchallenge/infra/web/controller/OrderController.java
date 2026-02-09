package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.impl.order.OrderManagementUseCase;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderResponseDTO;
import br.com.fiap.techchallenge.infra.web.dto.order.UpdateOrderRequest;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderResponseMapper;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderManagementUseCase orderManagementUseCase;

    public OrderController(OrderManagementUseCase orderManagementUseCase) {
        this.orderManagementUseCase = orderManagementUseCase;
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<CreateOrderResponseDTO> create(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CreateOrderRequest request) {

        CreateOrderCommand command = OrderWebMapper.toCommand(userId, request);
        Order created = orderManagementUseCase.create(command);

        return ResponseEntity.ok(OrderResponseMapper.from(created));
    }

    // --- LIST ALL ---
    @GetMapping
    public ResponseEntity<List<CreateOrderResponseDTO>> list() {
        List<Order> orders = orderManagementUseCase.listAll();
        List<CreateOrderResponseDTO> response = orders.stream()
                .map(OrderResponseMapper::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<CreateOrderResponseDTO> update(
            @PathVariable String orderId,
            @RequestBody UpdateOrderRequest request) {

        var command = OrderWebMapper.toUpdateCommand(request);
        Order updated = orderManagementUseCase.update(orderId, command);

        return ResponseEntity.ok(OrderResponseMapper.from(updated));
    }

    // --- DELETE ---
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable String orderId) {
        orderManagementUseCase.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
