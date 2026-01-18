package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindUserAddressByAddressIdUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private FindUserAddressByAddressIdUseCaseImpl findUserAddressByAddressIdUseCase;

    private UserAddress userAddress1;
    private UserAddress userAddress2;
    private String userId = "user123";
    private String addressId = "addr123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAddress1 = new UserAddress("ua1", userId, addressId, AddressType.HOME, "Home", true);
        userAddress2 = new UserAddress("ua2", "user456", addressId, AddressType.WORK, "Work", false);
    }

    @Test
    void shouldReturnListOfUserAddressesWhenTheyExist() {
        when(userAddressRepository.findByAddressId(addressId)).thenReturn(List.of(userAddress1, userAddress2));

        List<UserAddress> userAddresses = findUserAddressByAddressIdUseCase.execute(addressId);

        assertNotNull(userAddresses);
        assertEquals(2, userAddresses.size());
        assertEquals(userAddress1.getId(), userAddresses.get(0).getId());
        assertEquals(userAddress2.getId(), userAddresses.get(1).getId());
        verify(userAddressRepository, times(1)).findByAddressId(addressId);
    }

    @Test
    void shouldReturnEmptyListWhenNoUserAddressesExist() {
        when(userAddressRepository.findByAddressId(addressId)).thenReturn(Collections.emptyList());

        List<UserAddress> userAddresses = findUserAddressByAddressIdUseCase.execute(addressId);

        assertNotNull(userAddresses);
        assertTrue(userAddresses.isEmpty());
        verify(userAddressRepository, times(1)).findByAddressId(addressId);
    }
}
