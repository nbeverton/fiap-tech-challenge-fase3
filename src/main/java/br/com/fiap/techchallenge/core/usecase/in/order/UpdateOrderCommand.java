package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record UpdateOrderCommand(
        List<OrderItem> items,
        BigDecimal totalAmount
) {}
