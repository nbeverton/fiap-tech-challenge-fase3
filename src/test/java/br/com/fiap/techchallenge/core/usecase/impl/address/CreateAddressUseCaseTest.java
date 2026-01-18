package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private CreateAddressUseCaseImpl createAddressUseCase;

    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address("addressId123",
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
    void shouldCreateAddressSuccessfully() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address createdAddress = createAddressUseCase.execute(address);

        assertNotNull(createdAddress);
        assertEquals(address.getId(), createdAddress.getId());
        assertEquals(address.getStreetName(), createdAddress.getStreetName());
        verify(addressRepository, times(1)).save(any(Address.class));
    }
}
