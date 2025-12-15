package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.MenuDto;

public class MenuMapper {

    public static Menu toDomain(MenuDto dto, Restaurant restaurant) {
        return Menu.create(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInAvailable(),
                dto.imageUrl(),
                restaurant
        );
    }

}
