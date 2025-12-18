package br.com.fiap.techchallenge.infra.web.dto;

import java.util.List;

public record RestaurantDto(
        String id,
        String name,
        String addressId,
        String cuisineType,
        OpeningHoursDto openingHours,
        String userId,
        List<MenuDto> menu
) {
    public record OpeningHoursDto(String opens, String closes) {}
}