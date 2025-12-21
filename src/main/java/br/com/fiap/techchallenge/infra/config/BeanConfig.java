package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.restaurant.CreateRestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.DeleteRestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.FindRestaurantByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.ListRestaurantsUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.UpdateRestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.CreateMenuUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.DeleteMenuUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.FindMenuByIdUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.ListMenusUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.ListMenusByRestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.menu.UpdateMenuUseCaseImpl;

import br.com.fiap.techchallenge.core.usecase.in.restaurant.CreateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.DeleteRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.ListRestaurantsUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.UpdateRestaurantUseCase;

import br.com.fiap.techchallenge.core.usecase.in.menu.CreateMenuUseCase;
import br.com.fiap.techchallenge.core.usecase.in.menu.DeleteMenuUseCase;
import br.com.fiap.techchallenge.core.usecase.in.menu.FindMenuByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.menu.ListMenusUseCase;
import br.com.fiap.techchallenge.core.usecase.in.menu.ListMenusByRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.menu.UpdateMenuUseCase;

import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // ===================== Restaurant UseCases =====================

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepositoryPort repository) {
        return new CreateRestaurantUseCaseImpl(repository);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantRepositoryPort repository) {
        return new UpdateRestaurantUseCaseImpl(repository);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepositoryPort repository) {
        return new DeleteRestaurantUseCaseImpl(repository);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantRepositoryPort repository) {
        return new FindRestaurantByIdUseCaseImpl(repository);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantRepositoryPort repository) {
        return new ListRestaurantsUseCaseImpl(repository);
    }

    // ===================== Menu UseCases =====================

    @Bean
    public ListMenusUseCase listMenusUseCase(MenuRepositoryPort repository) {
        return new ListMenusUseCaseImpl(repository);
    }

    @Bean
    public ListMenusByRestaurantUseCase listMenusByRestaurantUseCase(MenuRepositoryPort repository) {
        return new ListMenusByRestaurantUseCaseImpl(repository);
    }

    @Bean
    public CreateMenuUseCase createMenuUseCase(MenuRepositoryPort repository) {
        return new CreateMenuUseCaseImpl(repository);
    }

    @Bean
    public UpdateMenuUseCase updateMenuUseCase(MenuRepositoryPort repository) {
        return new UpdateMenuUseCaseImpl(repository);
    }

    @Bean
    public DeleteMenuUseCase deleteMenuUseCase(MenuRepositoryPort repository) {
        return new DeleteMenuUseCaseImpl(repository);
    }

    @Bean
    public FindMenuByIdUseCase findMenuByIdUseCase(MenuRepositoryPort repository) {
        return new FindMenuByIdUseCaseImpl(repository);
    }

}

