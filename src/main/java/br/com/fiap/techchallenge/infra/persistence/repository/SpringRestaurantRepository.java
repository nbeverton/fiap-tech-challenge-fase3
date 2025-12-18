package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.entity.RestaurantEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringRestaurantRepository extends MongoRepository<RestaurantEntity, String> {}

