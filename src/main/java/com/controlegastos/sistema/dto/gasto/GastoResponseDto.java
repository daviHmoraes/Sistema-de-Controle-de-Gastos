package com.controlegastos.sistema.dto.gasto;

import com.controlegastos.sistema.dto.categoria.CategoriaResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoResponseDto(
        Long id,
        LocalDate data,
        String descricao,
        BigDecimal valor,
        CategoriaResponseDto categoria
) {}
