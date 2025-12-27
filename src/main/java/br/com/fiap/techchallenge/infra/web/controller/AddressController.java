package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.*;
import br.com.fiap.techchallenge.infra.web.dto.address.AddressResponse;
import br.com.fiap.techchallenge.infra.web.dto.address.CreateAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.address.UpdateAddressRequest;
import br.com.fiap.techchallenge.infra.web.mapper.address.AddressDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final FindAddressByIdUseCase findAddressByIdUseCase;
    private final FindAllAddressUseCase findAllAddressUseCase;


    public AddressController(
            CreateAddressUseCase createAddressUseCase,
            UpdateAddressUseCase updateAddressUseCase,
            DeleteAddressUseCase deleteAddressUseCase,
            FindAddressByIdUseCase findAddressByIdUseCase,
            FindAllAddressUseCase findAllAddressUseCase) {

        this.createAddressUseCase = createAddressUseCase;
        this.updateAddressUseCase = updateAddressUseCase;
        this.deleteAddressUseCase = deleteAddressUseCase;
        this.findAddressByIdUseCase = findAddressByIdUseCase;
        this.findAllAddressUseCase = findAllAddressUseCase;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CreateAddressRequest input
    ) {
        Address address = AddressDtoMapper.toDomain(input, userId);
        Address saved = createAddressUseCase.execute(address);
        return ResponseEntity
                .status(201)
                .body(AddressDtoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateAddressRequest input
    ) {
        Address addressToUpdate = updateAddressUseCase.execute(
                id,
                AddressDtoMapper.toDomain(input, id, userId)
        );
        return ResponseEntity.ok(AddressDtoMapper.toResponse(addressToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        deleteAddressUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId
    ) {
        return findAddressByIdUseCase.execute(id, userId)
                .map(AddressDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll(
            @RequestHeader("X-User-Id") String userId
    ) {
        List<AddressResponse> addresses = findAllAddressUseCase.execute(userId)
                .stream()
                .map(AddressDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(addresses);
    }
}
