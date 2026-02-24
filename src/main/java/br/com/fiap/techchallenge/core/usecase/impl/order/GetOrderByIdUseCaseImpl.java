package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderAccessDeniedException;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.GetOrderByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetOrderByIdUseCaseImpl implements GetOrderByIdUseCase {

    private final OrderRepositoryPort orderRepository;

    public GetOrderByIdUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order execute(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new OrderAccessDeniedException();
        }

        String requesterUserId = auth.getPrincipal().toString();

        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin && !order.getUserId().equals(requesterUserId)) {
            throw new OrderAccessDeniedException();
        }

        return order;
    }
}
