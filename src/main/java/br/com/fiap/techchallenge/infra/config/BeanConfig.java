package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.in.restaurant.RestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.MenuUseCase;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.RestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.impl.restaurant.MenuUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RestaurantUseCase restaurantUseCase(RestaurantRepositoryPort restaurantRepositoryPort) {
        return new RestaurantUseCaseImpl(restaurantRepositoryPort);
    }

    @Bean
    public MenuUseCase menuUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new MenuUseCaseImpl(menuRepositoryPort);
    }
}
