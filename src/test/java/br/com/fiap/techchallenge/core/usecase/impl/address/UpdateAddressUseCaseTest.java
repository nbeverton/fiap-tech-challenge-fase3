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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private UpdateAddressUseCaseImpl updateAddressUseCase;

    private Address existingAddress;
    private Address updatedAddress;
    private final String addressId = "addressId123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingAddress = new Address(addressId,
                "01001000",
                "Old Street",
                1,
                "Old Info",
                "Old Neighborhood",
                "Old City",
                "Old State",
                "Old Country");

        updatedAddress = new Address(addressId,
                "01001001",
                "New Street",
                2,
                "New Info",
                "New Neighborhood",
                "New City",
                "New State",
                "New Country");
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            assertEquals(updatedAddress.getPostalCode(), address.getPostalCode());
            assertEquals(updatedAddress.getStreetName(), address.getStreetName());
            assertEquals(updatedAddress.getStreetNumber(), address.getStreetNumber());
            assertEquals(updatedAddress.getAdditionalInfo(), address.getAdditionalInfo());
            assertEquals(updatedAddress.getNeighborhood(), address.getNeighborhood());
            assertEquals(updatedAddress.getCity(), address.getCity());
            assertEquals(updatedAddress.getStateProvince(), address.getStateProvince());
            assertEquals(updatedAddress.getCountry(), address.getCountry());
            return address;
        });

        Address result = updateAddressUseCase.execute(addressId, updatedAddress);

        assertNotNull(result);
        assertEquals(updatedAddress.getStreetName(), result.getStreetName());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class,
                () -> updateAddressUseCase.execute(addressId, updatedAddress));
        verify(addressRepository, never()).save(any(Address.class));
    }
}
