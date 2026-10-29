package com.controlegastos.sistema.service;

import com.controlegastos.sistema.dto.gasto.GastoPostRequestDto;
import com.controlegastos.sistema.dto.gasto.GastoResponseDto;
import com.controlegastos.sistema.entity.Categoria;
import com.controlegastos.sistema.entity.Gasto;
import com.controlegastos.sistema.exceptions.RecursoNaoEncontradoException;
import com.controlegastos.sistema.mapper.GastoMapper;
import com.controlegastos.sistema.repository.CategoriaRepository;
import com.controlegastos.sistema.repository.GastoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final GastoMapper gastoMapper;

    public GastoService(GastoRepository gastoRepository, CategoriaRepository categoriaRepository, GastoMapper gastoMapper) {
        this.gastoRepository = gastoRepository;
        this.categoriaRepository = categoriaRepository;
        this.gastoMapper = gastoMapper;
    }

    @Transactional
    public GastoResponseDto criar(GastoPostRequestDto dto) {
        Categoria categoria = buscarCategoria(dto.categoriaId());
        Gasto salvo = gastoRepository.save(gastoMapper.toEntity(dto, categoria));
        return gastoMapper.toDto(salvo);
    }

    @Transactional
    public GastoResponseDto atualizar(Long id, GastoPostRequestDto dto) {
        Gasto gasto = buscarGasto(id);
        Categoria categoria = buscarCategoria(dto.categoriaId());

        gasto.setData(dto.data());
        gasto.setDescricao(dto.descricao());
        gasto.setValor(dto.valor());
        gasto.setCategoria(categoria);

        return gastoMapper.toDto(gastoRepository.save(gasto));
    }

    @Transactional
    public void excluir(Long id) {
        gastoRepository.delete(buscarGasto(id));
    }

    @Transactional
    public Page<GastoResponseDto> listarPorMes(YearMonth mes, Pageable pageable) {
        return gastoRepository
                .findByDataBetweenOrderByDataDescIdDesc(mes.atDay(1), mes.atEndOfMonth(), pageable)
                .map(gastoMapper::toDto);
    }

    @Transactional
    public Page<GastoResponseDto> listarTodos(Pageable pageable) {
        return gastoRepository
                .findAll(pageable)
                .map(gastoMapper::toDto);
    }

    private Gasto buscarGasto(Long id) {
        return gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gasto não encontrado: id " + id));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: id " + id));
    }
}
