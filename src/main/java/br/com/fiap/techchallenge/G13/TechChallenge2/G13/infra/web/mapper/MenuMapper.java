package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.MenuDto;

public class MenuMapper {

    public static Menu toDomain(MenuDto dto) {
        if (dto == null) return null;
        if (dto.id() == null) {
            return Menu.create(
                    dto.name(),
                    dto.description(),
                    dto.price(),
                    dto.dineInAvailable(),
                    dto.imageUrl()
            );
        }
        return new Menu(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInAvailable(),
                dto.imageUrl()
        );
    }

    public static MenuDto toDto(Menu menu) {
        if (menu == null) return null;
        return new MenuDto(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.isDineInAvailable(),
                menu.getImageUrl()
        );
    }
}
