package br.com.fiap.techchallenge.core.domain.exception.openinghours;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidOpeningHoursException extends BusinessException {

    public InvalidOpeningHoursException(String message){
        super(message);
    }

}
