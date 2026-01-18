package br.com.fiap.techchallenge.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI dsmovieAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API JavaEats")
                        .description("API responsável pelo gerenciamento de restaurantes")
                        .version("v1.0.0")
                        .license(new License()
                                .url("https://github.com/nbeverton/fiap-tech-challenge-fase2")));
    }
}
