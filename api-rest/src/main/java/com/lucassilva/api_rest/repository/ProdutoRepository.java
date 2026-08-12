package com.lucassilva.api_rest.repository;

import com.lucassilva.api_rest.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
