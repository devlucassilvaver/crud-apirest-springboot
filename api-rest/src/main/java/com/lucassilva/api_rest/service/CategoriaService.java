package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.dto.request.CadastroCategoriaDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResponseDTO;
import com.lucassilva.api_rest.dto.response.ProdutoResponseDTO;
import com.lucassilva.api_rest.exception.CategoriaNaoEncontradaException;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private CategoriaResponseDTO categoria;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public void adicionarCategoria(CadastroCategoriaDTO cadastroCategoriaDTO){
        Categoria categoria = new Categoria();
        categoria.setNome(cadastroCategoriaDTO.getNome());
        categoriaRepository.save(categoria);
    }

    public CategoriaResponseDTO buscarCategoriaPorId(int id){

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria não encontrada."
                        )
                );

        List<ProdutoResponseDTO> produtos = categoria.getProdutos()
                .stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getPreco(),
                        null
                ))
                .toList();
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                produtos
        );
    }
}
