package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.exception.security.UnauthorizedException;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class FindUserByIdUseCaseImpl implements FindUserByIdUseCase {

    private final UserRepositoryPort userRepository;

    public FindUserByIdUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User execute(String id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        String requesterUserId = auth.getPrincipal().toString();

        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));

        if (!isAdmin && !requesterUserId.equals(id)) {
            throw new ForbiddenException("Forbidden: you can only access your own user data");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
