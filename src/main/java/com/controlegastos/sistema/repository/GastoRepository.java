package com.controlegastos.sistema.repository;

import com.controlegastos.sistema.entity.Gasto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @EntityGraph(attributePaths = "categoria")
    Page<Gasto> findByDataBetweenOrderByDataDescIdDesc(LocalDate inicio, LocalDate fim, Pageable pageable);

    @EntityGraph(attributePaths = "categoria")
    Page<Gasto> findAll(Pageable pageable);
}
