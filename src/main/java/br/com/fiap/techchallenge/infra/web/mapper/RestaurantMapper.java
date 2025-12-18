package br.com.fiap.techchallenge.infra.web.mapper;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.domain.model.OpeningHours;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.infra.web.dto.MenuDto;
import br.com.fiap.techchallenge.infra.web.dto.RestaurantDto;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static Restaurant toDomain(RestaurantDto dto) {
        if (dto == null) return null;
        CuisineType cuisine = CuisineType.valueOf(
                dto.cuisineType() == null ? "OTHER" : dto.cuisineType().toUpperCase()
        );
        OpeningHours opening = new OpeningHours(
                dto.openingHours() == null ? "00:00" : dto.openingHours().opens(),
                dto.openingHours() == null ? "00:00" : dto.openingHours().closes()
        );
        List<Menu> menu = dto.menu() == null ? List.of() : dto.menu().stream()
                .map(MenuMapper::toDomain)
                .collect(Collectors.toList());

        if (dto.id() == null) {
            return Restaurant.create(
                    dto.name(),
                    dto.addressId(),
                    cuisine,
                    opening,
                    dto.userId(),
                    menu
            );
        }

        return new Restaurant(
                dto.id(),
                dto.name(),
                dto.addressId(),
                cuisine,
                opening,
                dto.userId(),
                menu
        );
    }

    public static RestaurantDto toDto(Restaurant domain) {
        if (domain == null) return null;
        List<MenuDto> menuDtos = domain.getMenu().stream()
                .map(m -> new MenuDto(
                        m.getId(),
                        m.getName(),
                        m.getDescription(),
                        m.getPrice(),
                        m.isDineInAvailable(),
                        m.getImageUrl()
                ))
                .collect(Collectors.toList());

        RestaurantDto.OpeningHoursDto opening = new RestaurantDto.OpeningHoursDto(
                domain.getOpeningHours().getOpens(),
                domain.getOpeningHours().getCloses()
        );

        return new RestaurantDto(
                domain.getId(),
                domain.getName(),
                domain.getAddressId(),
                domain.getCuisineType().name(),
                opening,
                domain.getUserId(),
                menuDtos
        );
    }
}
