package br.com.fiap.techchallenge.core.usecase.out.security;

public interface TokenProviderPort {

    String generateToken(String userId, String userType);

}
