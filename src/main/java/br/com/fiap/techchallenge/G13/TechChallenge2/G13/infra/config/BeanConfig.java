package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.config;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.MenuUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.impl.RestaurantUseCaseImpl;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.impl.MenuUseCaseImpl;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.MenuRepositoryPort;
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
