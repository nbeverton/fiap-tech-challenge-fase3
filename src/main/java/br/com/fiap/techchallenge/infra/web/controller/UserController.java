package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.infra.web.dto.user.CreateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UserResponse;
import br.com.fiap.techchallenge.infra.web.mapper.user.UserDtoMapper;
import br.com.fiap.techchallenge.core.usecase.in.user.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;


    public UserController(
            CreateUserUseCase createUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase
    ){
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest input){

        User user = createUserUseCase.execute(UserDtoMapper.toDomain(input));

        return ResponseEntity.status(201).body(UserDtoMapper.toResponse(user));
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

        return findUserByIdUseCase.execute(id)
                .map(UserDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @RequestBody UpdateUserRequest input
            ){

        User userUpdated = updateUserUseCase.execute(
                id,
                UserDtoMapper.toDomain(input)
        );

        return ResponseEntity.ok(UserDtoMapper.toResponse(userUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){

        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
