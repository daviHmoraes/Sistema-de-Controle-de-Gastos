package com.controlegastos.sistema.mapper;

import com.controlegastos.sistema.dto.categoria.CategoriaPostRequestDto;
import com.controlegastos.sistema.dto.categoria.CategoriaResponseDto;
import com.controlegastos.sistema.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaPostRequestDto dto) {
        return new Categoria(dto.nome());
    }

    public CategoriaResponseDto toDto(Categoria entity) {
        return new CategoriaResponseDto(
                entity.getId(),
                entity.getNome()
        );
    }

}
