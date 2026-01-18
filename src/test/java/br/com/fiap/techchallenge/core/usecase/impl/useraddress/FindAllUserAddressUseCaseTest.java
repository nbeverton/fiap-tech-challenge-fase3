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

class FindAllUserAddressUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private FindAllUserAddressUseCaseImpl findAllUserAddressUseCase;

    private UserAddress userAddress1;
    private UserAddress userAddress2;
    private String userId = "user123";
    private String addressId1 = "addr1";
    private String addressId2 = "addr2";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAddress1 = new UserAddress("ua1", userId, addressId1, AddressType.HOME, "Home", true);
        userAddress2 = new UserAddress("ua2", userId, addressId2, AddressType.WORK, "Work", false);
    }

    @Test
    void shouldReturnListOfUserAddressesWhenTheyExist() {
        when(userAddressRepository.findAll()).thenReturn(List.of(userAddress1, userAddress2));

        List<UserAddress> userAddresses = findAllUserAddressUseCase.execute();

        assertNotNull(userAddresses);
        assertEquals(2, userAddresses.size());
        assertEquals(userAddress1.getId(), userAddresses.get(0).getId());
        assertEquals(userAddress2.getId(), userAddresses.get(1).getId());
        verify(userAddressRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUserAddressesExist() {
        when(userAddressRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserAddress> userAddresses = findAllUserAddressUseCase.execute();

        assertNotNull(userAddresses);
        assertTrue(userAddresses.isEmpty());
        verify(userAddressRepository, times(1)).findAll();
    }
}
