package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    private User existingUser;
    private UserAddress userAddress1;
    private UserAddress userAddress2;
    private final String userId = "user123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = new Client(userId,
                "Test User",
                "test@example.com",
                "testuser",
                "password123");

        userAddress1 = new UserAddress("ua1",
                userId, "address1",
                AddressType.HOME, "Home",
                true);

        userAddress2 = new UserAddress("ua2",
                userId,
                "address2",
                AddressType.WORK,
                "Work",
                false);
    }

    @Test
    void shouldDeleteUserSuccessfullyWithAddresses() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(userAddress1, userAddress2));
        doNothing().when(userAddressRepository).deleteById(anyString());
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> deleteUserUseCase.execute(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userAddressRepository, times(1)).findByUserId(userId);
        verify(userAddressRepository, times(2)).deleteById(anyString());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldDeleteUserSuccessfullyWithoutAddresses() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of());
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> deleteUserUseCase.execute(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userAddressRepository, times(1)).findByUserId(userId);
        verify(userAddressRepository, never()).deleteById(anyString());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> deleteUserUseCase.execute(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userAddressRepository, never()).findByUserId(anyString());
        verify(userAddressRepository, never()).deleteById(anyString());
        verify(userRepository, never()).deleteById(anyString());
    }
}
