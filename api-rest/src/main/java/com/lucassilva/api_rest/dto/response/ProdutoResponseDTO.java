package com.lucassilva.api_rest.dto.response;

public class ProdutoResponseDTO {
    private int id;
    private String nome;
    private double preco;
    private CategoriaResumoResponseDTO categoria;

    public ProdutoResponseDTO(
            int id,
            String nome,
            double preco,
            CategoriaResumoResponseDTO categoria) {

        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public CategoriaResumoResponseDTO getCategoria() {
        return categoria;
    }
}
