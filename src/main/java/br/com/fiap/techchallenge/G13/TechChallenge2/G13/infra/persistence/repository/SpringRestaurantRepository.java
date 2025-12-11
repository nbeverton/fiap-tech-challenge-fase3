package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;

public interface SpringRestaurantRepository
        extends JpaRepository<RestaurantEntity, Long> {}
