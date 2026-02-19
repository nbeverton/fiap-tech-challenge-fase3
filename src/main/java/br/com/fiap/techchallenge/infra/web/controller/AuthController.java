package br.com.fiap.techchallenge.infra.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.techchallenge.core.usecase.in.auth.LoginCommand;
import br.com.fiap.techchallenge.core.usecase.in.auth.LoginUseCase;
import br.com.fiap.techchallenge.infra.web.dto.auth.LoginRequestDTO;
import br.com.fiap.techchallenge.infra.web.dto.auth.LoginResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {

        var result = loginUseCase.login(
                new LoginCommand(
                        request.login(),
                        request.password()
                )
        );

        return ResponseEntity.ok(
                new LoginResponseDTO(result.userId(), result.token())
        );
    }
}

