package com.controlegastos.sistema.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaPostRequestDto(
        @NotBlank
        @Size(max = 30)
        String nome
) {}
