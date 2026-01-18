package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAllUsersUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private FindAllUsersUseCaseImpl findAllUsersUseCase;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new Client("1",
                "User One",
                "user1@example.com",
                "userone",
                "password");

        user2 = new Client("2",
                "User Two",
                "user2@example.com",
                "usertwo",
                "password");
    }

    @Test
    void shouldReturnListOfUsersWhenTheyExist() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = findAllUsersUseCase.execute();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user2.getName(), users.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = findAllUsersUseCase.execute();

        assertNotNull(users);
        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}
