package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAddressByIdUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private FindAddressByIdUseCaseImpl findAddressByIdUseCase;

    private Address existingAddress;
    private String addressId = "addressId123";

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
    void shouldFindAddressByIdSuccessfully() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));

        Address foundAddress = findAddressByIdUseCase.execute(addressId);

        assertNotNull(foundAddress);
        assertEquals(existingAddress.getId(), foundAddress.getId());
        assertEquals(existingAddress.getStreetName(), foundAddress.getStreetName());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> findAddressByIdUseCase.execute(addressId));
        verify(addressRepository, times(1)).findById(addressId);
    }
}
