package br.com.fiap.techchallenge.core.usecase.impl.auth;

import br.com.fiap.techchallenge.core.usecase.in.auth.LoginCommand;
import br.com.fiap.techchallenge.core.usecase.in.auth.LoginResult;
import br.com.fiap.techchallenge.core.usecase.in.auth.LoginUseCase;
import br.com.fiap.techchallenge.core.usecase.out.FindUserByLoginPort;
import br.com.fiap.techchallenge.core.usecase.out.security.PasswordEncoderPort;
import br.com.fiap.techchallenge.core.usecase.out.security.TokenProviderPort;

public class LoginUseCaseImpl implements LoginUseCase {

    private final FindUserByLoginPort findUserByLoginPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public LoginUseCaseImpl(
            FindUserByLoginPort findUserByLoginPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort) {
        this.findUserByLoginPort = findUserByLoginPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public LoginResult login(LoginCommand command) {

        var user = findUserByLoginPort.findByLogin(command.login())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getUserType().equals(br.com.fiap.techchallenge.core.domain.enums.UserType.CLIENT)) {
            throw new RuntimeException("Only CLIENT can authenticate");
        }

        boolean valid = passwordEncoderPort.matches(
                command.password(),
                user.getPassword());

        if (!valid) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = tokenProviderPort.generateToken(
                user.getId(),
                user.getUserType().name());

        return new LoginResult(user.getId(), token);
    }
}
