package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class MissingMenuIdException extends BusinessException {

    public MissingMenuIdException() {
        super("menuId must not be null/blank");
    }
}
