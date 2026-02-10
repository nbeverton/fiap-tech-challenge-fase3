package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class MenuDoesNotBelongToRestaurantException extends BusinessException {

    public MenuDoesNotBelongToRestaurantException(String menuId, String restaurantId) {

        super("Menu item " + menuId + " does not belong to restaurant " + restaurantId);
    }
}
