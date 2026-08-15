package com.lucassilva.api_rest.dto.response;

public class CategoriaResumoResponseDTO {

    private int id;
    private String nome;

    public CategoriaResumoResponseDTO(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
