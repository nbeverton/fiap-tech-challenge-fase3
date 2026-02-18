//package br.com.fiap.techchallenge.core.usecase.impl.order;
//
//import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
//import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
//import br.com.fiap.techchallenge.core.domain.exception.order.*;
//import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
//import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
//import br.com.fiap.techchallenge.core.domain.model.Address;
//import br.com.fiap.techchallenge.core.domain.model.Order;
//import br.com.fiap.techchallenge.core.domain.model.Restaurant;
//import br.com.fiap.techchallenge.core.domain.model.UserAddress;
//import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
//import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
//import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
//import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
//import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderItemCommand;
//import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderCommand;
//import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
//import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
//import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
//import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class OrderManagementUseCase {
//
//    private final OrderRepositoryPort orderRepository;
//    private final UserAddressRepositoryPort userAddressRepository;
//    private final AddressRepositoryPort addressRepository;
//    private final RestaurantRepositoryPort restaurantRepository;
//
//    public OrderManagementUseCase(
//            OrderRepositoryPort orderRepository,
//            UserAddressRepositoryPort userAddressRepository,
//            AddressRepositoryPort addressRepository,
//            RestaurantRepositoryPort restaurantRepository
//    ) {
//        this.orderRepository = orderRepository;
//        this.userAddressRepository = userAddressRepository;
//        this.addressRepository = addressRepository;
//        this.restaurantRepository = restaurantRepository;
//    }
//
//    public Order create(CreateOrderCommand command) {
//
//        // 0) Busca o vínculo e verifica se o endereço é principal
//        UserAddress userAddress = userAddressRepository.findUserAddressById(command.userAddressId())
//                .orElseThrow(() -> new UserAddressNotFoundException( command.userAddressId()));
//
//        if(!userAddress.isPrincipal()){
//            throw new CannotCreateOrderWithoutPrimaryAddress();
//        }
//
//        String userId = userAddress.getUserId();
//
//        // 1) Snapshot do endereço (usa o userAddress já carregado)
//        Address address = addressRepository.findById(userAddress.getAddressId())
//                .orElseThrow(() -> new AddressNotFoundException(userAddress.getAddressId()));
//
//        DeliveryAddressSnapshot snapshot = new DeliveryAddressSnapshot(
//                address.getStreetName(),
//                address.getStreetNumber(),
//                address.getNeighborhood(),
//                address.getCity(),
//                address.getStateProvince(),
//                address.getPostalCode(),
//                address.getAdditionalInfo()
//        );
//
//        // 2) Restaurant + itens + total
//        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
//                .orElseThrow(() -> new RestaurantNotFoundException(command.restaurantId()));
//
//        List<OrderItem> items = buildOrderItemsFromRestaurantMenu(restaurant, command.items());
//        BigDecimal totalAmount = calculateTotal(items);
//
//        Instant now = Instant.now();
//
//        Order order = new Order(
//                UUID.randomUUID().toString(),
//                command.restaurantId(),
//                userId,
//                command.userAddressId(),
//                snapshot,
//                items,
//                totalAmount,
//                OrderStatus.CREATED,
//                now,
//                now
//        );
//
//        return orderRepository.save(order);
//    }
//
//    public List<Order> listAll() {
//        return orderRepository.findAll();
//    }
//
//    /**
//     * Update: altera apenas itens + totalAmount.
//     * Respeita regra de status (até certo status pode, depois bloqueia).
//     */
//    public Order update(String orderId, UpdateOrderCommand command) {
//        Order existing = orderRepository.findById(orderId)
//                .orElseThrow(() -> new OrderNotFoundException(orderId));
//
//        if (!canEditItems(existing.getOrderStatus())) {
//            throw new InvalidOrderStatusException("Order cannot be edited when status is " + existing.getOrderStatus());
//        }
//
//        Restaurant restaurant = restaurantRepository.findById(existing.getRestaurantId())
//                .orElseThrow(() -> new RestaurantNotFoundException(existing.getRestaurantId()));
//
//        List<OrderItem> newItems = buildOrderItemsFromRestaurantMenu(restaurant, command.items());
//        BigDecimal newTotalAmount = calculateTotal(newItems);
//
//        Order updated = new Order(
//                existing.getId(),
//                existing.getRestaurantId(),
//                existing.getUserId(),
//                existing.getUserAddressId(),
//                existing.getDeliveryAddress(),      // snapshot não muda
//                newItems,
//                newTotalAmount,
//                existing.getOrderStatus(),          // status não muda aqui
//                existing.getCreatedAt(),
//                Instant.now()
//        );
//
//        return orderRepository.save(updated);
//    }
//
//    public void delete(String orderId) {
//        if (!orderRepository.existsById(orderId)) {
//            throw new OrderNotFoundException("Order not found: " + orderId);
//        }
//        orderRepository.deleteById(orderId);
//    }
//
//    // -------------------------
//    // Helpers
//    // -------------------------
//
//    private DeliveryAddressSnapshot buildDeliverySnapshot(String userAddressId) {
//        UserAddress userAddress = userAddressRepository.findUserAddressById(userAddressId)
//                .orElseThrow(() -> new UserAddressNotFoundException(userAddressId));
//
//        Address address = addressRepository.findById(userAddress.getAddressId())
//                .orElseThrow(() -> new AddressNotFoundException(userAddress.getAddressId()));
//
//        return new DeliveryAddressSnapshot(
//                address.getStreetName(),
//                address.getStreetNumber(),
//                address.getNeighborhood(),
//                address.getCity(),
//                address.getStateProvince(),
//                address.getPostalCode(),
//                address.getAdditionalInfo()
//        );
//    }
//
//    private List<OrderItem> buildOrderItemsFromRestaurantMenu(Restaurant restaurant, List<CreateOrderItemCommand> itemCommands) {
//        if (itemCommands == null || itemCommands.isEmpty()) {
//            throw new EmptyOrderItemsException();
//        }
//
//        List<OrderItem> items = new ArrayList<>();
//
//        for (CreateOrderItemCommand cmd : itemCommands) {
//            if (cmd.menuId() == null || cmd.menuId().isBlank()) {
//                throw new MissingMenuIdException();
//            }
//            if (cmd.quantity() == null || cmd.quantity() <= 0) {
//                throw new InvalidOrderItemQuantityException(cmd.menuId());
//            }
//
//            Menu menu = findMenuOrThrow(restaurant, cmd.menuId());
//
//            // snapshot do menu no pedido (name/price vindo do Menu)
//            items.add(new OrderItem(
//                    menu.getId(),         // OrderItem.menuItemId = menu.id
//                    menu.getName(),
//                    cmd.quantity(),
//                    menu.getPrice()
//            ));
//        }
//
//        return items;
//    }
//
//    private Menu findMenuOrThrow(Restaurant restaurant, String menuId) {
//        for (Menu m : restaurant.getMenu()) {
//            if (m.getId() != null && m.getId().equals(menuId)) {
//                return m;
//            }
//        }
//        throw new MenuDoesNotBelongToRestaurantException(menuId, restaurant.getId());
//    }
//
//    private BigDecimal calculateTotal(List<OrderItem> items) {
//        BigDecimal total = BigDecimal.ZERO;
//        for (OrderItem item : items) {
//            total = total.add(item.getTotal());
//        }
//        return total;
//    }
//
//    private boolean canEditItems(OrderStatus status) {
//
//        return status == OrderStatus.CREATED
//                || status == OrderStatus.AWAITING_PAYMENT;
//    }
//}
