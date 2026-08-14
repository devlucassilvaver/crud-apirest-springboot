package com.lucassilva.api_rest.dto.response;

import java.util.List;

public class CategoriaResponseDTO {
    private int id;
    private String nome;
    private List<ProdutoResponseDTO> produtos;

    public CategoriaResponseDTO(int id, String nome, List<ProdutoResponseDTO> produtos) {
        this.id = id;
        this.nome = nome;
        this.produtos = produtos;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<ProdutoResponseDTO> getProdutos() {
        return produtos;
    }
}
