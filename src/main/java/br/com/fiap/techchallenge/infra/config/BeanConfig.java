package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.MenuUseCase;
import br.com.fiap.techchallenge.core.usecase.in.impl.RestaurantUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.impl.MenuUseCaseImpl;
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
