package br.com.fiap.techchallenge.infra.web.exception;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.address.InvalidAddressException;
import br.com.fiap.techchallenge.core.domain.exception.menu.InvalidMenuException;
import br.com.fiap.techchallenge.core.domain.exception.openinghours.InvalidOpeningHoursException;
import br.com.fiap.techchallenge.core.domain.exception.order.EmptyOrderItemsException;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderItemQuantityException;
import br.com.fiap.techchallenge.core.domain.exception.order.MenuDoesNotBelongToRestaurantException;
import br.com.fiap.techchallenge.core.domain.exception.order.MissingMenuIdException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;
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

    // // 1) 404 – "not found"
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 2) 400 – input and domain validation errors
    @ExceptionHandler({
            InvalidUserException.class,
            InvalidMenuException.class,
            InvalidUserAddressException.class,
            InvalidAddressException.class,
            InvalidOpeningHoursException.class,
            EmptyOrderItemsException.class,
            InvalidOrderItemQuantityException.class,
            MissingMenuIdException.class,
            MenuDoesNotBelongToRestaurantException.class,
            InvalidPaymentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleInvalidInput(BusinessException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3) 409 – business conflicts (duplicates, constraints, etc.)
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            RestaurantAlreadyExistsException.class,
            CannotDeletePrimaryAddressException.class,
            BusinessException.class // fallback para outras BusinessException não mapeadas acima
    })
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 3.5) 400 – JSON parsing / object binding errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        // Procura uma BusinessException na "causa raiz"
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

        // Generic malformed request body error
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 4) 500 – generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
