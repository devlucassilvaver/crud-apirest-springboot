package com.lucassilva.api_rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AtualizacaoProdutoDTO {
    @NotBlank
    private String nome;

    @Positive
    private double preco;

    @NotNull
    @Positive
    private Integer categoriaId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }
}
