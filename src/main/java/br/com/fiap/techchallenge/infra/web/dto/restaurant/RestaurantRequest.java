package br.com.fiap.techchallenge.infra.web.dto.restaurant;

import java.math.BigDecimal;
import java.util.List;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuRequest;

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

}
