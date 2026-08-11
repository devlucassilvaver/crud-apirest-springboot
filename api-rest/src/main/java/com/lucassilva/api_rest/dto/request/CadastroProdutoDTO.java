package com.lucassilva.api_rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CadastroProdutoDTO {
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @Positive(message = "O preço deve ser maior que zero.")
    private double preco;

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
}
