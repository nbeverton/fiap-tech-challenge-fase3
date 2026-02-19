package br.com.fiap.techchallenge.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.techchallenge.core.usecase.impl.auth.LoginUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.auth.LoginUseCase;
import br.com.fiap.techchallenge.core.usecase.out.FindUserByLoginPort;
import br.com.fiap.techchallenge.core.usecase.out.security.PasswordEncoderPort;
import br.com.fiap.techchallenge.core.usecase.out.security.TokenProviderPort;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(
            FindUserByLoginPort findUserByLoginPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort
    ) {
        return new LoginUseCaseImpl(
                findUserByLoginPort,
                passwordEncoderPort,
                tokenProviderPort
        );
    }

}

