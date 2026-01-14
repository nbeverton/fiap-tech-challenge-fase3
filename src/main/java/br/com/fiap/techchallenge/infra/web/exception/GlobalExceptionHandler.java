package br.com.fiap.techchallenge.infra.web.exception;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.address.InvalidAddressException;
import br.com.fiap.techchallenge.core.domain.exception.menu.InvalidMenuException;
import br.com.fiap.techchallenge.core.domain.exception.openinghours.InvalidOpeningHoursException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.user.InvalidUserException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.InvalidUserAddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) Tudo que é "não encontrado" → 404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 2) Validações de entrada → 400
    @ExceptionHandler({
            InvalidUserException.class,
            InvalidMenuException.class,
            InvalidUserAddressException.class,
            InvalidAddressException.class,
            InvalidOpeningHoursException.class
    })
    public ResponseEntity<ApiErrorResponse> handleInvalidInput(BusinessException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    // 3) Outras regras de negócio → 409 (duplicidade, não pode apagar, etc)
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            RestaurantAlreadyExistsException.class,
            CannotDeletePrimaryAddressException.class,
            BusinessException.class // fallback para qualquer regra de negócio não mapeada acima
    })
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 3.5) Erros de parsing JSON / bind para objetos → tentar extrair BusinessException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        // Procura uma BusinessException (ou filha) na cadeia de causas
        Throwable cause = ex.getCause();
        BusinessException businessCause = null;

        while (cause != null) {
            if (cause instanceof BusinessException be) {
                businessCause = be;
                break;
            }
            cause = cause.getCause();
        }

        if (businessCause != null) {
            ApiErrorResponse error = new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    businessCause.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Se não for BusinessException, trata como corpo malformado genérico
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    // 4) Fallback genérico → 500 (algo escapou não mapeado)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
