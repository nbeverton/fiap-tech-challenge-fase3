package br.com.fiap.techchallenge.infra.web.dto.restaurant;

import java.util.List;

public record RestaurantResponse(
        String id,
        String name,
        String addressId,
        String cuisineType,
        OpeningHoursResponse openingHours,
        String userId,
        List<MenuResponse> menu
) {

    public record OpeningHoursResponse(
            String opens,
            String closes
    ) {}

    public record MenuResponse(
            String id,
            String name,
            String description,
            double price,
            boolean dineInAvailable,
            String imageUrl
    ) {}
}
