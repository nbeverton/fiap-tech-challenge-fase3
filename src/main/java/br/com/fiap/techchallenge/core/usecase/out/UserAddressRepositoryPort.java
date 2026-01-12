package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepositoryPort {

    UserAddress save(UserAddress userAddress);

    void deleteById(String id);

    List<UserAddress> findByAddressId(String addressId);

    List<UserAddress> findPrincipalsByAddressId(String addressId);

    List<UserAddress> findPrincipalsById(String id);

    List<UserAddress> findByUserId(String userId);

    Optional<UserAddress> findPrincipalByUserId(String userId);

    Optional<UserAddress> findUserAddressById(String id);
}
