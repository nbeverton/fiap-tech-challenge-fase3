package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindUserByIdUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private FindUserByIdUseCaseImpl findUserByIdUseCase;

    private User existingUser;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = new Client(userId, "Test User", "test@example.com", "testuser", "password123");
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User foundUser = findUserByIdUseCase.execute(userId);

        assertNotNull(foundUser);
        assertEquals(existingUser.getId(), foundUser.getId());
        assertEquals(existingUser.getName(), foundUser.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> findUserByIdUseCase.execute(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}
