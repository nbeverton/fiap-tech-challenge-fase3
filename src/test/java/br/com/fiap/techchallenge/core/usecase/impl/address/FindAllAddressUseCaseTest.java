package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAllAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private FindAllAddressUseCaseImpl findAllAddressUseCase;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address1 = new Address("1",
                "01001000",
                "Street One",
                1,
                "",
                "Neighborhood1",
                "City1",
                "State1",
                "Country1");

        address2 = new Address("2",
                "01001001",
                "Street Two",
                2,
                "",
                "Neighborhood2",
                "City2",
                "State2",
                "Country2");
    }

    @Test
    void shouldReturnListOfAddressesWhenTheyExist() {
        when(addressRepository.findAll()).thenReturn(List.of(address1, address2));

        List<Address> addresses = findAllAddressUseCase.execute();

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        assertEquals(address1.getStreetName(), addresses.get(0).getStreetName());
        assertEquals(address2.getStreetName(), addresses.get(1).getStreetName());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAddressesExist() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        List<Address> addresses = findAllAddressUseCase.execute();

        assertNotNull(addresses);
        assertTrue(addresses.isEmpty());
        verify(addressRepository, times(1)).findAll();
    }
}
