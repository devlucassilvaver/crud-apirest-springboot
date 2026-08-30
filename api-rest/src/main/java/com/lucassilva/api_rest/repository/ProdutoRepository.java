package com.lucassilva.api_rest.repository;

import com.lucassilva.api_rest.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    Page<Produto> findByCategoriaId(Integer categoriaId, Pageable pageable);

    Page<Produto> findByNomeContaining(String nome, Pageable pageable);
}
