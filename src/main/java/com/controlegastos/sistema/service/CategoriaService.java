package com.controlegastos.sistema.service;

import com.controlegastos.sistema.dto.categoria.CategoriaPostRequestDto;
import com.controlegastos.sistema.dto.categoria.CategoriaResponseDto;
import com.controlegastos.sistema.entity.Categoria;
import com.controlegastos.sistema.exceptions.NomeDuplicadoException;
import com.controlegastos.sistema.mapper.CategoriaMapper;
import com.controlegastos.sistema.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository,
                                             CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    public CategoriaResponseDto insert(CategoriaPostRequestDto request) {

        if(categoriaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new NomeDuplicadoException(request.nome());
        }

        Categoria categoria = categoriaMapper.toEntity(request);

        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    public Page<CategoriaResponseDto> findAll(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(categoriaMapper::toDto);
    }

}