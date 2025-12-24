package br.com.fiap.techchallenge.infra.web.controller.query;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.FindUserAddressByAddressIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.FindUserAddressByUserIdUseCase;
import br.com.fiap.techchallenge.infra.web.dto.address.AddressResponse;
import br.com.fiap.techchallenge.infra.web.dto.user.UserResponse;
import br.com.fiap.techchallenge.infra.web.mapper.address.AddressDtoMapper;
import br.com.fiap.techchallenge.infra.web.mapper.user.UserDtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/queries")
public class UserAddressQueryController {

    private final FindUserAddressByAddressIdUseCase findUserAddressByAddressIdUseCase;
    private final FindUserAddressByUserIdUseCase findUserAddressByUserIdUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAddressByIdUseCase findAddressByIdUseCase;

    public UserAddressQueryController(FindUserAddressByAddressIdUseCase findUserAddressByAddressIdUseCase, FindUserAddressByUserIdUseCase findUserAddressByUserIdUseCase, FindUserByIdUseCase findUserByIdUseCase, FindAddressByIdUseCase findAddressByIdUseCase) {

        this.findUserAddressByAddressIdUseCase = findUserAddressByAddressIdUseCase;
        this.findUserAddressByUserIdUseCase = findUserAddressByUserIdUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAddressByIdUseCase = findAddressByIdUseCase;
    }

    @GetMapping("/users/{userId}/addresses")
    public List<AddressResponse> getAddressByUser(
            @PathVariable String userId
    ){
        List<UserAddress> links =
                findUserAddressByUserIdUseCase.execute(userId);

        return links.stream()
                .map(link -> findAddressByIdUseCase.execute(link.getAddressId())
                        .orElseThrow(() ->
                                new RuntimeException("Address not found for id: " + link.getAddressId())
                        )
                )
                .map(AddressDtoMapper::toResponse)
                .toList();
    }


    @GetMapping("/addresses/{addressId}/users")
    public List<UserResponse> getUserByAddress(
            @PathVariable String addressId
    ){
        List<UserAddress> links =
                findUserAddressByAddressIdUseCase.execute(addressId);

        return links.stream()
                .map(link -> findUserByIdUseCase.execute(link.getUserId())
                        .orElseThrow(() ->
                                new RuntimeException("User not found for id: " + link.getUserId())
                        )
                )
                .map(UserDtoMapper::toResponse)
                .toList();
    }
}
