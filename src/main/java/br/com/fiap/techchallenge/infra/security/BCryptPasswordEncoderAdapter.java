package br.com.fiap.techchallenge.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.techchallenge.core.usecase.out.security.PasswordEncoderPort;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String raw) {
        return encoder.encode(raw);
    }

    @Override
    public boolean matches(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
