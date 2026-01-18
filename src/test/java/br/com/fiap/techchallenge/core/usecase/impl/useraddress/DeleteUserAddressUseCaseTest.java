package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteUserAddressUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @Mock
    private AddressRepositoryPort addressRepository; // Injected but not used in execute method, still needs to be mocked.

    @InjectMocks
    private DeleteUserAddressUseCaseImpl deleteUserAddressUseCase;

    private UserAddress existingUserAddress;
    private String userAddressId = "uaId123";
    private String userId = "user123";
    private String addressId = "address123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUserAddress = new UserAddress(userAddressId, userId, addressId, AddressType.HOME, "Home", false);
    }

    @Test
    void shouldDeleteUserAddressSuccessfully() {
        when(userAddressRepository.findUserAddressById(userAddressId)).thenReturn(Optional.of(existingUserAddress));
        when(userAddressRepository.findPrincipalsById(userAddressId)).thenReturn(Collections.emptyList());
        doNothing().when(userAddressRepository).deleteById(userAddressId);

        assertDoesNotThrow(() -> deleteUserAddressUseCase.execute(userAddressId));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId);
        verify(userAddressRepository, times(1)).findPrincipalsById(userAddressId);
        verify(userAddressRepository, times(1)).deleteById(userAddressId);
    }

    @Test
    void shouldThrowExceptionWhenUserAddressNotFound() {
        when(userAddressRepository.findUserAddressById(userAddressId)).thenReturn(Optional.empty());

        assertThrows(UserAddressNotFoundException.class, () -> deleteUserAddressUseCase.execute(userAddressId));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId);
        verify(userAddressRepository, never()).findPrincipalsById(anyString());
        verify(userAddressRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserAddressIsPrimary() {
        UserAddress primaryUserAddress = new UserAddress(userAddressId, userId, addressId, AddressType.HOME, "Home", true);

        when(userAddressRepository.findUserAddressById(userAddressId)).thenReturn(Optional.of(primaryUserAddress));
        when(userAddressRepository.findPrincipalsById(userAddressId)).thenReturn(List.of(primaryUserAddress));

        assertThrows(CannotDeletePrimaryAddressException.class, () -> deleteUserAddressUseCase.execute(userAddressId));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId);
        verify(userAddressRepository, times(1)).findPrincipalsById(userAddressId);
        verify(userAddressRepository, never()).deleteById(anyString());
    }
}
