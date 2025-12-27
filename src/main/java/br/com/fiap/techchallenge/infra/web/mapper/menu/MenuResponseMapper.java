package br.com.fiap.techchallenge.infra.web.mapper.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuResponse;

public class MenuResponseMapper {

    private MenuResponseMapper() {}

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


