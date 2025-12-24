package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.CreateUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.DeleteUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.UpdateUserAddressUseCase;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.CreateUserAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UpdateUserAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UserAddressResponse;
import br.com.fiap.techchallenge.infra.web.mapper.useraddress.UserAddressDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user-addresses")
public class UserAddressController {

    private final CreateUserAddressUseCase createUserAddressUseCase;
    private final DeleteUserAddressUseCase deleteUserAddressUseCase;
    private final UpdateUserAddressUseCase updateUserAddressUseCase;

    public UserAddressController(
            CreateUserAddressUseCase createUserAddressUseCase,
            DeleteUserAddressUseCase deleteUserAddressUseCase,
            UpdateUserAddressUseCase updateUserAddressUseCase
    ) {
        this.createUserAddressUseCase = createUserAddressUseCase;
        this.deleteUserAddressUseCase = deleteUserAddressUseCase;
        this.updateUserAddressUseCase = updateUserAddressUseCase;
    }


    @PostMapping
    public ResponseEntity<UserAddressResponse> create(
            @RequestBody CreateUserAddressRequest request
    ){

        UserAddress domain =
                UserAddressDtoMapper.toDomain(request);

        UserAddress created =
                createUserAddressUseCase.execute(domain);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserAddressDtoMapper.toResponse(created));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){

        deleteUserAddressUseCase.execute(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserAddressResponse> update(
            @PathVariable String id,
            @RequestBody UpdateUserAddressRequest request
    ){

        UserAddress update =
                updateUserAddressUseCase.execute(
                        id,
                        AddressType.valueOf(request.type()),
                        request.label(),
                        request.principal()
                );

        return ResponseEntity.ok(
                UserAddressDtoMapper.toResponse(update)
        );
    }
}
