package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.dto.request.CadastroCategoriaDTO;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public void adicionarCategoria(CadastroCategoriaDTO cadastroCategoriaDTO){
        Categoria categoria = new Categoria();
        categoria.setNome(cadastroCategoriaDTO.getNome());
        categoriaRepository.save(categoria);
    }
}
