package com.controlegastos.sistema.dto.gasto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoPostRequestDto(
        @NotNull
        @PastOrPresent
        LocalDate data,

        @NotBlank
        @Size(max = 150)
        String descricao,

        @Positive
        BigDecimal valor,

        @NotNull
        @Positive
        Long categoriaId
) {}
