package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.AddAddressToUserInput;
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

class AddAddressToUserUseCaseTest {

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private UserAddressRepositoryPort userAddressRepository;

    @InjectMocks
    private AddAddressToUserUseCaseImpl addAddressToUserUseCase;

    private AddAddressToUserInput input;
    private User user;
    private Address newAddress;
    private String userId = "user123";
    private String newAddressId = "addr456";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        input = new AddAddressToUserInput(
                userId,
                newAddressId,
                AddressType.HOME,
                "New Home"
        );
        user = new Client(userId, "Test User", "test@example.com", "testuser", "password123");
        newAddress = new Address(newAddressId, "01001000", "New Street", 1, "", "New Neighborhood", "New City", "New State", "New Country");
    }

    @Test
    void shouldAddAddressToUserSuccessfullyAndMakeItPrimary() {
        doReturn(user).when(findUserByIdUseCase).execute(userId);
        when(addressRepository.findById(newAddressId)).thenReturn(Optional.of(newAddress));
        when(userAddressRepository.findPrincipalByUserId(userId)).thenReturn(Optional.empty());
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(
                new UserAddress("newUaId", userId, newAddressId, AddressType.HOME, "New Home", true)
        ));
        when(addressRepository.findById(newAddressId)).thenReturn(Optional.of(newAddress));


        List<UserAddressSummaryOutput> result = addAddressToUserUseCase.execute(input);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).principal());
        assertEquals(newAddress.getStreetName(), result.get(0).streetName());
        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(addressRepository, times(2)).findById(newAddressId); // Called once for validation, once for summary
        verify(userAddressRepository, times(1)).findPrincipalByUserId(userId);
        verify(userAddressRepository, times(1)).save(any(UserAddress.class)); // For new link
        verify(userAddressRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldAddAddressToUserSuccessfullyAndMakeItPrimaryAndDemotePreviousPrimary() {
        UserAddress oldPrimary = new UserAddress("oldUaId", userId, "oldAddrId", AddressType.WORK, "Old Work", true);
        Address oldAddress = new Address("oldAddrId", "01001000", "Old Street", 10, "", "Old Hood", "Old City", "Old State", "Old Country");

        doReturn(user).when(findUserByIdUseCase).execute(userId);
        when(addressRepository.findById(newAddressId)).thenReturn(Optional.of(newAddress));
        when(userAddressRepository.findPrincipalByUserId(userId)).thenReturn(Optional.of(oldPrimary));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userAddressRepository.findByUserId(userId)).thenReturn(List.of(
                new UserAddress("newUaId", userId, newAddressId, AddressType.HOME, "New Home", true),
                new UserAddress("oldUaId", userId, "oldAddrId", AddressType.WORK, "Old Work", false)
        ));
        when(addressRepository.findById(newAddressId)).thenReturn(Optional.of(newAddress));
        when(addressRepository.findById("oldAddrId")).thenReturn(Optional.of(oldAddress));


        List<UserAddressSummaryOutput> result = addAddressToUserUseCase.execute(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).principal()); // New address is primary
        assertEquals(newAddress.getStreetName(), result.get(0).streetName());
        assertFalse(result.get(1).principal()); // Old address is not primary

        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(addressRepository, times(3)).findById(anyString()); // Once for new, once for old, once for validation
        verify(userAddressRepository, times(1)).findPrincipalByUserId(userId);
        verify(userAddressRepository, times(2)).save(any(UserAddress.class));
        verify(userAddressRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        doThrow(new UserNotFoundException(userId)).when(findUserByIdUseCase).execute(userId);

        assertThrows(UserNotFoundException.class, () -> addAddressToUserUseCase.execute(input));

        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(addressRepository, never()).findById(anyString());
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
        verify(userAddressRepository, never()).findByUserId(anyString());
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        doReturn(user).when(findUserByIdUseCase).execute(userId);
        when(addressRepository.findById(newAddressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addAddressToUserUseCase.execute(input));

        verify(findUserByIdUseCase, times(1)).execute(userId);
        verify(addressRepository, times(1)).findById(newAddressId);
        verify(userAddressRepository, never()).findPrincipalByUserId(anyString());
        verify(userAddressRepository, never()).save(any(UserAddress.class));
        verify(userAddressRepository, never()).findByUserId(anyString());
    }
}
