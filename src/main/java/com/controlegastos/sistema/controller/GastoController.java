package com.controlegastos.sistema.controller;

import com.controlegastos.sistema.dto.gasto.GastoPostRequestDto;
import com.controlegastos.sistema.dto.gasto.GastoResponseDto;
import com.controlegastos.sistema.service.GastoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @PostMapping
    public ResponseEntity<GastoResponseDto> criar(@Valid @RequestBody GastoPostRequestDto dto) {
        GastoResponseDto criado = gastoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public ResponseEntity<Page<GastoResponseDto>> listar(@RequestParam(value = "mes", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
                                                         @PageableDefault(size = 20, sort = {"data", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<GastoResponseDto> response = (mes != null) ? gastoService.listarPorMes(mes, pageable) : gastoService.listarTodos(pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody GastoPostRequestDto dto) {
        return ResponseEntity.ok(gastoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        gastoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
