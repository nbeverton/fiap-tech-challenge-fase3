package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.user.CreateUserInput;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private CreateUserInput createUserInput;
    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createUserInput = new CreateUserInput(
                "Test User",
                UserType.OWNER,
                "test@example.com",
                "testuser",
                "password123",
                "addressId123",
                AddressType.WORK,
                "Work Address"
        );

        address = new Address("addressId123",
                "01001000",
                "Street",
                1,
                "",
                "Neighborhood",
                "City",
                "State",
                "Country");

        user = new Client("userId123",
                createUserInput.name(),
                createUserInput.email(),
                createUserInput.login(),
                createUserInput.password());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.findByLogin(createUserInput.login())).thenReturn(Optional.empty());
        when(addressRepository.findById(createUserInput.addressId())).thenReturn(Optional.of(address));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userAddressRepository.save(any(UserAddress.class))).thenReturn(any(UserAddress.class));

        User createdUser = createUserUseCase.execute(createUserInput);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(createUserInput.login(), createdUser.getLogin());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    void shouldThrowExceptionWhenUserLoginAlreadyExists() {
        when(userRepository.findByLogin(createUserInput.login())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> createUserUseCase.execute(createUserInput));
        verify(userRepository, never()).save(any(User.class));
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        when(userRepository.findByLogin(createUserInput.login())).thenReturn(Optional.empty());
        when(addressRepository.findById(createUserInput.addressId())).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> createUserUseCase.execute(createUserInput));
        verify(userRepository, never()).save(any(User.class));
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }
}
