package br.com.fiap.techchallenge.infra.web.dto;

public record MenuDto(
        String id,
        String name,
        String description,
        double price,
        boolean dineInAvailable,
        String imageUrl
) {
}
