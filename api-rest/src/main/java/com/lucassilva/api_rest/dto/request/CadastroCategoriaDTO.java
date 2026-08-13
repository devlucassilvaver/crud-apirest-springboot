package com.lucassilva.api_rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CadastroCategoriaDTO {
    @NotBlank
    private String nome;

    public CadastroCategoriaDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
