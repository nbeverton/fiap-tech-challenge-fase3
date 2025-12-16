package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto;

public record MenuDto(
        String id,
        String name,
        String description,
        double price,
        boolean dineInAvailable,
        String imageUrl
) {
}
