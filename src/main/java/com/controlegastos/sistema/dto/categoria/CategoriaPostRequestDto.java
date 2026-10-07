package com.controlegastos.sistema.dto.categoria;

import jakarta.validation.constraints.NotBlank;
public record CategoriaPostRequestDto(
        @NotBlank String nome
) {}
