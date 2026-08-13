package com.lucassilva.api_rest.repository;

import com.lucassilva.api_rest.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
