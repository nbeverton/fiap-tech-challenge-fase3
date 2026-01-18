package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.RestaurantDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringRestaurantRepository
        extends MongoRepository<RestaurantDocument, String> {

    Optional<RestaurantDocument> findByAddressId(String addressId);

    Optional<RestaurantDocument> findByName(String name);

    boolean existsByUserId(String userId);

}

