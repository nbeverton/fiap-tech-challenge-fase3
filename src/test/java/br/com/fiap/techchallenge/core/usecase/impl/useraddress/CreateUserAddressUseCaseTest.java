package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateUserAddressUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private FindAddressByIdUseCase findAddressByIdUseCase;

    @InjectMocks
    private CreateUserAddressUseCaseImpl createUserAddressUseCase;

    private UserAddress userAddress;
    private User user;
    private Address address;
    private String userId = "user123";
    private String addressId = "address123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAddress = new UserAddress(userId, addressId, AddressType.HOME, "Home", true);
        user = new Client(userId, "Test User", "test@example.com", "testuser", "password123");
        address = new Address(addressId, "01001000", "Street", 1, "", "Neighborhood", "City", "State", "Country");
    }

    @Test
    void shouldCreateUserAddressSuccessfully() {
        doReturn(user).when(findUserByIdUseCase).execute(userId);
        doReturn(address).when(findAddressByIdUseCase).execute(addressId);
        when(userAddressRepository.save(any(UserAddress.class))).thenReturn(userAddress);

        UserAddress createdUserAddress = createUserAddressUseCase.execute(userAddress);

        assertNotNull(createdUserAddress);
        assertEquals(userAddress.getUserId(), createdUserAddress.getUserId());
        assertEquals(userAddress.getAddressId(), createdUserAddress.getAddressId());
        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(findAddressByIdUseCase, times(1)).execute(addressId);
        verify(userAddressRepository, times(1)).save(userAddress);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        doThrow(new UserNotFoundException(userId)).when(findUserByIdUseCase).execute(userId);

        assertThrows(UserNotFoundException.class, () -> createUserAddressUseCase.execute(userAddress));

        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(findAddressByIdUseCase, never()).execute(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        doReturn(user).when(findUserByIdUseCase).execute(userId);
        doThrow(new AddressNotFoundException(addressId)).when(findAddressByIdUseCase).execute(addressId);

        assertThrows(AddressNotFoundException.class, () -> createUserAddressUseCase.execute(userAddress));

        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(findAddressByIdUseCase, times(1)).execute(addressId);
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }
}
