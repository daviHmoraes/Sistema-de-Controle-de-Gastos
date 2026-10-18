package com.controlegastos.sistema.controller;

import com.controlegastos.sistema.dto.categoria.CategoriaPostRequestDto;
import com.controlegastos.sistema.dto.categoria.CategoriaResponseDto;
import com.controlegastos.sistema.service.CategoriaService;
import jakarta.servlet.ServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> post(@RequestBody CategoriaPostRequestDto request, ServletRequest servletRequest) {
        CategoriaResponseDto response = categoriaService.insert(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDto>> findAll(Pageable pageable) {
        Page<CategoriaResponseDto> page = categoriaService.findAll(pageable);

        return ResponseEntity
                .ok(page);
    }
}
