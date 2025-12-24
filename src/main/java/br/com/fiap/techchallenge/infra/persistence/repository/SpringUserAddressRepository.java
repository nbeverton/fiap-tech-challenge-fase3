package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.UserAddressDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpringUserAddressRepository extends MongoRepository<UserAddressDocument, String> {

    List<UserAddressDocument> findByUserId(String userId);

    List<UserAddressDocument> findByAddressId(String addressId);

    Optional<UserAddressDocument> findByUserIdAndPrincipalTrue(String userId);


}
