package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.*;
import br.com.fiap.techchallenge.core.usecase.in.order.dto.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.dto.UpdateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.status.*;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderResponseDTO;
import br.com.fiap.techchallenge.infra.web.dto.order.UpdateOrderRequest;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderResponseMapper;
import br.com.fiap.techchallenge.infra.web.mapper.order.OrderWebMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;

    private final MarkOrderAsAcceptedUseCase acceptOrderUseCase;
    private final MarkOrderAsDeliveredUseCase deliverOrderUseCase;
    private final MarkOrderAsOutForDeliveryUseCase outForDeliveryOrderUseCase;
    private final MarkOrderAsRejectedUseCase rejectOrderUseCase;
    private final MarkOrderAsPreparingUseCase startPreparingOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           GetOrderByIdUseCase getOrderByIdUseCase,
                           ListOrdersUseCase listOrdersUseCase,
                           UpdateOrderUseCase updateOrderUseCase,
                           DeleteOrderUseCase deleteOrderUseCase,
                           MarkOrderAsAcceptedUseCase acceptOrderUseCase,
                           MarkOrderAsDeliveredUseCase deliverOrderUseCase,
                           MarkOrderAsOutForDeliveryUseCase outForDeliveryOrderUseCase,
                           MarkOrderAsRejectedUseCase rejectOrderUseCase,
                           MarkOrderAsPreparingUseCase startPreparingOrderUseCase) {

        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;

        this.acceptOrderUseCase = acceptOrderUseCase;
        this.deliverOrderUseCase = deliverOrderUseCase;
        this.outForDeliveryOrderUseCase = outForDeliveryOrderUseCase;
        this.rejectOrderUseCase = rejectOrderUseCase;
        this.startPreparingOrderUseCase = startPreparingOrderUseCase;
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<CreateOrderResponseDTO> create(
            @RequestBody CreateOrderRequest request,
            Authentication authentication) {

        String clientId = (String) authentication.getPrincipal();

        CreateOrderCommand command = OrderWebMapper.toCommand(request, clientId);

        Order created = createOrderUseCase.execute(command);

        return ResponseEntity.ok(OrderResponseMapper.from(created));
    }

    // --- LIST BY ID ---
    @GetMapping("/{orderId}")
    public ResponseEntity<CreateOrderResponseDTO> getById(@PathVariable String orderId) {

        Order order = getOrderByIdUseCase.execute(orderId);

        return ResponseEntity.ok(
                OrderResponseMapper.from(order));
    }

    // --- LIST ALL ---
    @GetMapping
    public ResponseEntity<List<CreateOrderResponseDTO>> list() {
        List<Order> orders = listOrdersUseCase.execute();

        List<CreateOrderResponseDTO> response = orders.stream()
                .map(OrderResponseMapper::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    // --- UPDATE ---
    @PutMapping("/{orderId}")
    public ResponseEntity<CreateOrderResponseDTO> update(
            @PathVariable String orderId,
            @RequestBody UpdateOrderRequest request) {

        UpdateOrderCommand command = OrderWebMapper.toUpdateCommand(request);
        Order updated = updateOrderUseCase.execute(orderId, command);

        return ResponseEntity.ok(OrderResponseMapper.from(updated));
    }

    // --- DELETE ---
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable String orderId) {
        deleteOrderUseCase.execute(orderId);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // Change Status Endpoints
    // ---------------------------
    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<Void> accept(
            @PathVariable String orderId
    ){
        acceptOrderUseCase.accept(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable String orderId
    ){
        rejectOrderUseCase.reject(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/preparing")
    public ResponseEntity<Void> starPreparing(
            @PathVariable String orderId
    ){
        startPreparingOrderUseCase.startPreparing(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/out-for-delivery")
    public ResponseEntity<Void> outForDelivery(
            @PathVariable String orderId
    ){
        outForDeliveryOrderUseCase.outForDelivery(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<Void> deliver(
            @PathVariable String orderId
    ){
        deliverOrderUseCase.deliver(orderId);
        return ResponseEntity.noContent().build();
    }
}
