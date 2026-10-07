package com.controlegastos.sistema.dto.categoria;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public record CategoriaResponseDto (
        @Id Long id,
        @NotBlank String nome
) {}
