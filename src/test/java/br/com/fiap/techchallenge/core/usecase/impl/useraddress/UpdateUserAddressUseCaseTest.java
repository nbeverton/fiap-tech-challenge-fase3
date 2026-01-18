package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdateUserAddressUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private UpdateUserAddressUseCaseImpl updateUserAddressUseCase;

    private UserAddress existingNonPrimaryUserAddress;
    private UserAddress existingPrimaryUserAddress;
    private UserAddress otherNonPrimaryUserAddress;
    private String userId = "user123";
    private String addressId1 = "addr1";
    private String addressId2 = "addr2";
    private String userAddressId1 = "uaId1";
    private String userAddressId2 = "uaId2";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingNonPrimaryUserAddress = new UserAddress(userAddressId1, userId, addressId1, AddressType.HOME, "Home 1", false);
        existingPrimaryUserAddress = new UserAddress(userAddressId2, userId, addressId2, AddressType.WORK, "Work 1", true);
        otherNonPrimaryUserAddress = new UserAddress("uaId3", userId, "addr3", AddressType.OTHER, "Other", false);
    }

    @Test
    void shouldUpdateUserAddressSuccessfully() {
        // Update non-primary to non-primary, change type and label
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAddress result = updateUserAddressUseCase.execute(userAddressId1, AddressType.WORK, "Work Home", false);

        assertNotNull(result);
        assertEquals(AddressType.WORK, result.getType());
        assertEquals("Work Home", result.getLabel());
        assertFalse(result.isPrincipal());
        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    void shouldUpdateUserAddressAndSetAsPrimaryDemotingOldPrimary() {
        // Update non-primary to primary
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress));
        when(userAddressRepository.findPrincipalByUserId(userId)).thenReturn(Optional.of(existingPrimaryUserAddress)); // Simulate an old primary
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAddress result = updateUserAddressUseCase.execute(userAddressId1, AddressType.HOME, "Primary Home", true);

        assertNotNull(result);
        assertEquals(AddressType.HOME, result.getType());
        assertEquals("Primary Home", result.getLabel());
        assertTrue(result.isPrincipal());
        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, times(1)).findPrincipalByUserId(userId); // Called to find old primary
        verify(userAddressRepository, times(2)).save(any(UserAddress.class)); // Once for old primary, once for new primary
        assertFalse(existingPrimaryUserAddress.isPrincipal()); // Ensure old primary was demoted
    }

    @Test
    void shouldNotUpdateUserAddressWhenNoChanges() {
        // Keep non-primary, same type, same label
        UserAddress original = new UserAddress(userAddressId1, userId, addressId1, AddressType.HOME, "Home 1", false);
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(original));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAddress result = updateUserAddressUseCase.execute(userAddressId1, AddressType.HOME, "Home 1", false);

        assertNotNull(result);
        assertEquals(AddressType.HOME, result.getType());
        assertEquals("Home 1", result.getLabel());
        assertFalse(result.isPrincipal());
        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAddressNotFound() {
        when(userAddressRepository.findUserAddressById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserAddressNotFoundException.class, () -> updateUserAddressUseCase.execute(userAddressId1, AddressType.HOME, "Home 1", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    void shouldThrowExceptionWhenAttemptingToUnsetPrimaryAddress() {
        // Attempt to change an existing primary address to non-primary
        when(userAddressRepository.findUserAddressById(userAddressId2)).thenReturn(Optional.of(existingPrimaryUserAddress));

        assertThrows(CannotDeletePrimaryAddressException.class, () -> updateUserAddressUseCase.execute(userAddressId2, AddressType.WORK, "Work 1", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId2);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }
}
