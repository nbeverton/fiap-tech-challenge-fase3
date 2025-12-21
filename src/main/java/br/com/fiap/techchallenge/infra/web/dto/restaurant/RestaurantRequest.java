package br.com.fiap.techchallenge.infra.web.dto.restaurant;

import java.math.BigDecimal;
import java.util.List;

public record RestaurantRequest(
        String name,
        String addressId,
        String cuisineType,
        OpeningHoursRequest openingHours,
        String userId,
        List<MenuRequest> menu
) {

    public record OpeningHoursRequest(
            String opens,
            String closes
    ) {}

    public record MenuRequest(
            String name,
            String description,
            BigDecimal price,
            boolean dineInAvailable,
            String imageUrl
    ) {}
}
