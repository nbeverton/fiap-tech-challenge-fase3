package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.model.Address;
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

class DeleteAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private DeleteAddressUseCaseImpl deleteAddressUseCase;

    private Address existingAddress;
    private final String addressId = "addressId123";
    private final String userId = "userId123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingAddress = new Address(addressId,
                "01001000",
                "Street",
                1,
                "",
                "Neighborhood",
                "City",
                "State",
                "Country");
    }

    @Test
    void shouldDeleteAddressSuccessfullyWithoutLinks() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(userAddressRepository.findPrincipalsByAddressId(addressId)).thenReturn(Collections.emptyList());
        when(userAddressRepository.findByAddressId(addressId)).thenReturn(Collections.emptyList());
        doNothing().when(addressRepository).delete(addressId);

        assertDoesNotThrow(() -> deleteAddressUseCase.execute(addressId));

        verify(addressRepository, times(1)).findById(addressId);
        verify(userAddressRepository, times(1)).findPrincipalsByAddressId(addressId);
        verify(userAddressRepository, times(1)).findByAddressId(addressId);
        verify(userAddressRepository, never()).deleteById(anyString());
        verify(addressRepository, times(1)).delete(addressId);
    }

    @Test
    void shouldDeleteAddressSuccessfullyWithOnlySecondaryLinks() {
        UserAddress secondaryLink = new UserAddress("uaId1",
                userId,
                addressId,
                AddressType.WORK,
                "Work",
                false);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(userAddressRepository.findPrincipalsByAddressId(addressId)).thenReturn(Collections.emptyList());
        when(userAddressRepository.findByAddressId(addressId)).thenReturn(List.of(secondaryLink));
        doNothing().when(userAddressRepository).deleteById("uaId1");
        doNothing().when(addressRepository).delete(addressId);

        assertDoesNotThrow(() -> deleteAddressUseCase.execute(addressId));

        verify(addressRepository, times(1)).findById(addressId);
        verify(userAddressRepository, times(1)).findPrincipalsByAddressId(addressId);
        verify(userAddressRepository, times(1)).findByAddressId(addressId);
        verify(userAddressRepository, times(1)).deleteById("uaId1");
        verify(addressRepository, times(1)).delete(addressId);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> deleteAddressUseCase.execute(addressId));

        verify(addressRepository, times(1)).findById(addressId);
        verify(userAddressRepository, never()).findPrincipalsByAddressId(anyString());
        verify(userAddressRepository, never()).findByAddressId(anyString());
        verify(userAddressRepository, never()).deleteById(anyString());
        verify(addressRepository, never()).delete(anyString());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsPrimary() {
        UserAddress primaryLink = new UserAddress("uaId1",
                userId,
                addressId,
                AddressType.HOME,
                "Home",
                true);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(userAddressRepository.findPrincipalsByAddressId(addressId)).thenReturn(List.of(primaryLink));

        assertThrows(CannotDeletePrimaryAddressException.class, () -> deleteAddressUseCase.execute(addressId));

        verify(addressRepository, times(1)).findById(addressId);
        verify(userAddressRepository, times(1)).findPrincipalsByAddressId(addressId);
        verify(userAddressRepository, never()).findByAddressId(anyString());
        verify(userAddressRepository, never()).deleteById(anyString());
        verify(addressRepository, never()).delete(anyString());
    }
}
