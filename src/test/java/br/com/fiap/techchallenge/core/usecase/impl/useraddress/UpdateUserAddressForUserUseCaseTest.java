package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressSummaryOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdateUserAddressForUserUseCaseTest {

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private UpdateUserAddressForUserUseCaseImpl updateUserAddressForUserUseCase;

    private UserAddress existingNonPrimaryUserAddress;
    private UserAddress existingPrimaryUserAddress;
    private UserAddress otherUserAddressForDifferentUser;
    private Address associatedAddress1;
    private Address associatedAddress2;
    private String userId = "user123";
    private String differentUserId = "user456";
    private String addressId1 = "addr1";
    private String addressId2 = "addr2";
    private String userAddressId1 = "uaId1";
    private String userAddressId2 = "uaId2";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingNonPrimaryUserAddress = new UserAddress(userAddressId1, userId, addressId1, AddressType.HOME, "Home 1", false);
        existingPrimaryUserAddress = new UserAddress(userAddressId2, userId, addressId2, AddressType.WORK, "Work 1", true);
        otherUserAddressForDifferentUser = new UserAddress("uaId3", differentUserId, "addr3", AddressType.OTHER, "Other", false);

        associatedAddress1 = new Address(addressId1, "01001000", "Street 1", 1, "", "N1", "C1", "S1", "BR");
        associatedAddress2 = new Address(addressId2, "01002000", "Street 2", 2, "", "N2", "C2", "S2", "BR");
    }

    @Test
    void shouldUpdateUserAddressSuccessfully() {
        // Update non-primary to non-primary, change type and label
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(existingNonPrimaryUserAddress));
        when(addressRepository.findById(addressId1)).thenReturn(Optional.of(associatedAddress1));

        List<UserAddressSummaryOutput> result = updateUserAddressForUserUseCase.execute(userId, userAddressId1, AddressType.WORK, "Work Home", false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AddressType.WORK, result.get(0).addressType());

        assertFalse(result.get(0).principal());
        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString()); // Not setting as primary
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
        verify(userAddressRepository, times(1)).findByUserId(userId);
        verify(addressRepository, times(1)).findById(addressId1);
    }

    @Test
    void shouldUpdateUserAddressAndSetAsPrimaryDemotingOldPrimary() {
        // Update non-primary to primary
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress));
        when(userAddressRepository.findPrincipalByUserId(userId)).thenReturn(Optional.of(existingPrimaryUserAddress)); // Simulate an old primary
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0)); // For both saves
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(existingNonPrimaryUserAddress, existingPrimaryUserAddress));
        when(addressRepository.findById(addressId1)).thenReturn(Optional.of(associatedAddress1));
        when(addressRepository.findById(addressId2)).thenReturn(Optional.of(associatedAddress2));

        List<UserAddressSummaryOutput> result = updateUserAddressForUserUseCase.execute(userId, userAddressId1, AddressType.HOME, "Primary Home", true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).principal()); // New primary
        assertFalse(result.get(1).principal()); // Old primary demoted
        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, times(1)).findPrincipalByUserId(userId);
        verify(userAddressRepository, times(2)).save(any(UserAddress.class)); // Once for old primary, once for new primary
        verify(userAddressRepository, times(1)).findByUserId(userId);
        verify(addressRepository, times(2)).findById(anyString()); // Once for each address in summary
    }

    @Test
    void shouldThrowExceptionWhenUserAddressNotFound() {
        when(userAddressRepository.findUserAddressById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserAddressNotFoundException.class, () -> updateUserAddressForUserUseCase.execute(userId, userAddressId1, AddressType.HOME, "Home", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
        verify(userAddressRepository, never()).findByUserId(anyString());
        verify(addressRepository, never()).findById(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotOwnUserAddress() {
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress)); // userAddressId1 belongs to userId

        assertThrows(UserAddressNotFoundException.class, () -> updateUserAddressForUserUseCase.execute(differentUserId, userAddressId1, AddressType.HOME, "Home", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
        verify(userAddressRepository, never()).findByUserId(anyString());
        verify(addressRepository, never()).findById(anyString());
    }

    @Test
    void shouldThrowExceptionWhenAttemptingToUnsetPrimaryAddress() {
        when(userAddressRepository.findUserAddressById(userAddressId2)).thenReturn(Optional.of(existingPrimaryUserAddress)); // userAddressId2 is primary

        assertThrows(CannotDeletePrimaryAddressException.class, () -> updateUserAddressForUserUseCase.execute(userId, userAddressId2, AddressType.WORK, "Work 1", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId2);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
        verify(userAddressRepository, never()).findByUserId(anyString());
        verify(addressRepository, never()).findById(anyString());
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundDuringSummaryAssembly() {
        when(userAddressRepository.findUserAddressById(userAddressId1)).thenReturn(Optional.of(existingNonPrimaryUserAddress));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(existingNonPrimaryUserAddress));
        when(addressRepository.findById(addressId1)).thenReturn(Optional.empty()); // Address not found during summary assembly

        assertThrows(AddressNotFoundException.class, () -> updateUserAddressForUserUseCase.execute(userId, userAddressId1, AddressType.WORK, "Work Home", false));

        verify(userAddressRepository, times(1)).findUserAddressById(userAddressId1);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
        verify(userAddressRepository, times(1)).findByUserId(userId);
        verify(addressRepository, times(1)).findById(addressId1);
    }
}
