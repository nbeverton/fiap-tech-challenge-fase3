package br.com.fiap.techchallenge.core.usecase.out.security;

public interface PasswordEncoderPort {

    boolean matches(String raw, String encoded);

}

