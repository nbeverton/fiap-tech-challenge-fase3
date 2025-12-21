package br.com.fiap.techchallenge.infra.web.dto.menu;

import java.math.BigDecimal;

public record MenuRequest(
        String name,
        String description,
        BigDecimal price,
        boolean dineInAvailable,
        String imageUrl
) {}
