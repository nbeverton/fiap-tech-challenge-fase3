package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.AddAddressToUserUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.UpdateUserAddressForUserUseCase;
import br.com.fiap.techchallenge.infra.web.dto.user.CreateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UserResponse;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.AddAddressToUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UpdateUserAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UserAddressSummaryResponse;
import br.com.fiap.techchallenge.infra.web.mapper.user.UserDtoMapper;
import br.com.fiap.techchallenge.core.usecase.in.user.*;
import br.com.fiap.techchallenge.infra.web.mapper.user.CreateUserDtoMapper;
import br.com.fiap.techchallenge.infra.web.mapper.useraddress.AddAddressToUserDtoMapper;
import br.com.fiap.techchallenge.infra.web.mapper.useraddress.UpdateUserAddressDtoMapper;
import br.com.fiap.techchallenge.infra.web.mapper.useraddress.UserAddressSummaryDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final AddAddressToUserUseCase addAddressToUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdateUserAddressForUserUseCase updateUserAddressForUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;


    public UserController(
            CreateUserUseCase createUserUseCase, AddAddressToUserUseCase addAddressToUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            UpdateUserUseCase updateUserUseCase, UpdateUserAddressForUserUseCase updateUserAddressForUserUseCase,
            DeleteUserUseCase deleteUserUseCase
    ){
        this.createUserUseCase = createUserUseCase;
        this.addAddressToUserUseCase = addAddressToUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.updateUserAddressForUserUseCase = updateUserAddressForUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request){

        User user = createUserUseCase.execute(CreateUserDtoMapper.toInput(request));

        return ResponseEntity.status(201).body(UserDtoMapper.toResponse(user));
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<List<UserAddressSummaryResponse>> addAddressToUser(
            @PathVariable String userId,
            @RequestBody AddAddressToUserRequest request
            ){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        addAddressToUserUseCase.execute(
                                AddAddressToUserDtoMapper.toInput(userId,request)
                        )
                                .stream()
                                .map(UserAddressSummaryDtoMapper::toResponse)
                                .toList()
                );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){

        List<UserResponse> users = findAllUsersUseCase.execute()
                .stream()
                .map(UserDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id){

        User user = findUserByIdUseCase.execute(id);

        return ResponseEntity.ok(UserDtoMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @RequestBody UpdateUserRequest input
    ) {
        var useCaseInput = new UpdateUserInput(
                input.name(),
                input.email(),
                input.login(),
                input.password()
        );

        User userUpdated = updateUserUseCase.execute(id, useCaseInput);

        return ResponseEntity.ok(UserDtoMapper.toResponse(userUpdated));
    }


    @PutMapping("/{userId}/addresses/{userAddressId}")
    public ResponseEntity<List<UserAddressSummaryResponse>> updateUserAddress(
            @PathVariable String userId,
            @PathVariable String userAddressId,
            @RequestBody UpdateUserAddressRequest request
            ){

        return ResponseEntity.ok(
                updateUserAddressForUserUseCase.execute(
                        userId,
                        userAddressId,
                        UpdateUserAddressDtoMapper.toAddressType(request),
                        UpdateUserAddressDtoMapper.toLabel(request),
                        UpdateUserAddressDtoMapper.toPrincipal(request)
                )
                        .stream()
                        .map(UserAddressSummaryDtoMapper::toResponse)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){

        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
