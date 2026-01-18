package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private User existingUser;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = new Client(userId, "Old Name", "old@example.com", "oldlogin", "oldpassword");
    }

    @Test
    void shouldUpdateUserNameSuccessfully() {
        UpdateUserRequest request = new UpdateUserRequest("New Name", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("New Name", user.getName());
            return user;
        });

        User updatedUser = updateUserUseCase.execute(userId, request);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserEmailSuccessfully() {
        UpdateUserRequest request = new UpdateUserRequest(null, "new@example.com", null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("new@example.com", user.getEmail());
            return user;
        });

        User updatedUser = updateUserUseCase.execute(userId, request);

        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserLoginSuccessfully() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, "newlogin", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("newlogin", user.getLogin());
            return user;
        });

        User updatedUser = updateUserUseCase.execute(userId, request);

        assertNotNull(updatedUser);
        assertEquals("newlogin", updatedUser.getLogin());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserPasswordSuccessfully() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, null, "newpassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("newpassword", user.getPassword());
            return user;
        });

        User updatedUser = updateUserUseCase.execute(userId, request);

        assertNotNull(updatedUser);
        assertEquals("newpassword", updatedUser.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldNotUpdateUserWhenNoFieldsProvided() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = updateUserUseCase.execute(userId, request);

        assertNotNull(updatedUser);
        assertEquals(existingUser.getName(), updatedUser.getName());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());
        assertEquals(existingUser.getLogin(), updatedUser.getLogin());
        assertEquals(existingUser.getPassword(), updatedUser.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest("New Name", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.execute(userId, request));
        verify(userRepository, never()).save(any(User.class));
    }
}
