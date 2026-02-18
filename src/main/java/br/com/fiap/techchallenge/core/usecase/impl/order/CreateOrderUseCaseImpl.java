package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.order.CannotCreateOrderWithoutPrimaryAddress;
import br.com.fiap.techchallenge.core.domain.exception.order.EmptyOrderItemsException;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderItemQuantityException;
import br.com.fiap.techchallenge.core.domain.exception.order.MenuDoesNotBelongToRestaurantException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderItemCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final UserAddressRepositoryPort userAddressRepository;
    private final AddressRepositoryPort addressRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public CreateOrderUseCaseImpl(
            OrderRepositoryPort orderRepository,
            UserAddressRepositoryPort userAddressRepository,
            AddressRepositoryPort addressRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.orderRepository = orderRepository;
        this.userAddressRepository = userAddressRepository;
        this.addressRepository = addressRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Order execute(CreateOrderCommand command) {

        // 0) Busca o vínculo e verifica se o endereço é principal
        UserAddress userAddress = userAddressRepository.findUserAddressById(command.userAddressId())
                .orElseThrow(() -> new UserAddressNotFoundException(command.userAddressId()));

        if (!userAddress.isPrincipal()) {
            throw new CannotCreateOrderWithoutPrimaryAddress();
        }

        String userId = userAddress.getUserId();

        // 1) Snapshot do endereço
        Address address = addressRepository.findById(userAddress.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(userAddress.getAddressId()));

        DeliveryAddressSnapshot snapshot = new DeliveryAddressSnapshot(
                address.getStreetName(),
                address.getStreetNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getStateProvince(),
                address.getPostalCode(),
                address.getAdditionalInfo()
        );

        // 2) Restaurant + itens + total
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(command.restaurantId()));

        List<OrderItem> items = buildOrderItemsFromRestaurantMenu(restaurant, command.items());
        BigDecimal totalAmount = calculateTotal(items);

        Instant now = Instant.now();

        Order order = new Order(
                UUID.randomUUID().toString(),
                command.restaurantId(),
                userId,
                command.userAddressId(),
                snapshot,
                items,
                totalAmount,
                OrderStatus.CREATED,
                now,
                now
        );

        return orderRepository.save(order);
    }

    // -------------------------
    // Helpers locais (só deste use case)
    // -------------------------

    private List<OrderItem> buildOrderItemsFromRestaurantMenu(
            Restaurant restaurant,
            List<CreateOrderItemCommand> itemCommands
    ) {
        if (itemCommands == null || itemCommands.isEmpty()) {
            throw new EmptyOrderItemsException();
        }

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderItemCommand cmd : itemCommands) {
            if (cmd.menuId() == null || cmd.menuId().isBlank()) {
                throw new br.com.fiap.techchallenge.core.domain.exception.order.MissingMenuIdException();
            }
            if (cmd.quantity() == null || cmd.quantity() <= 0) {
                throw new InvalidOrderItemQuantityException(cmd.menuId());
            }

            Menu menu = findMenuOrThrow(restaurant, cmd.menuId());

            items.add(new OrderItem(
                    menu.getId(),
                    menu.getName(),
                    cmd.quantity(),
                    menu.getPrice()
            ));
        }

        return items;
    }

    private Menu findMenuOrThrow(Restaurant restaurant, String menuId) {
        for (Menu m : restaurant.getMenu()) {
            if (m.getId() != null && m.getId().equals(menuId)) {
                return m;
            }
        }
        throw new MenuDoesNotBelongToRestaurantException(menuId, restaurant.getId());
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getTotal());
        }
        return total;
    }

}
