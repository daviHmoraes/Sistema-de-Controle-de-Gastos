package com.controlegastos.sistema.repository;

import com.controlegastos.sistema.entity.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository repository;

    @Override
    public void run(String... args) {
        List<String> nomes = List.of("Mercado", "Transporte", "Moradia", "Lazer", "Saúde", "Outros");

        for(String nome : nomes) {
            if(!repository.existsByName(nome)) {
                repository.save(new Categoria(nome));
            }
        }

    }
}
