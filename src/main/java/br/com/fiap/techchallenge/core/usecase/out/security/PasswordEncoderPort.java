package br.com.fiap.techchallenge.core.usecase.out.security;

public interface PasswordEncoderPort {

    String encode(String raw);

    boolean matches(String raw, String encoded);

}


