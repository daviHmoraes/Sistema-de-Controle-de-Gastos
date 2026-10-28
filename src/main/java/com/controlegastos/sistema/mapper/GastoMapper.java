package com.controlegastos.sistema.mapper;

import com.controlegastos.sistema.dto.categoria.CategoriaResponseDto;
import com.controlegastos.sistema.dto.gasto.GastoPostRequestDto;
import com.controlegastos.sistema.dto.gasto.GastoResponseDto;
import com.controlegastos.sistema.entity.Categoria;
import com.controlegastos.sistema.entity.Gasto;
import org.springframework.stereotype.Component;

@Component
public class GastoMapper {

    public Gasto toEntity(GastoPostRequestDto dto, Categoria categoria) {
        return new Gasto().builder()
                .data(dto.data())
                .descricao(dto.descricao())
                .valor(dto.valor())
                .categoria(categoria)
                .build();
    }

    public GastoResponseDto toDto(Gasto gasto) {
        return new GastoResponseDto(
                gasto.getId(),
                gasto.getData(),
                gasto.getDescricao(),
                gasto.getValor(),
                new CategoriaResponseDto(
                        gasto.getCategoria().getId(),
                        gasto.getCategoria().getNome()
                )
        );
    }

}