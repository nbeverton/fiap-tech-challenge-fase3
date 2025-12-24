package br.com.fiap.techchallenge.infra.web.mapper.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuRequest;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuResponse;

public class MenuMapper {

    private MenuMapper(){}

    public static Menu toDomain(MenuRequest request) {
        return Menu.create(
                request.name(),
                request.description(),
                request.price(),
                request.dineInAvailable(),
                request.imageUrl()
        );
    }

    public static MenuResponse toResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.isDineInAvailable(),
                menu.getImageUrl()
        );
    }
}
