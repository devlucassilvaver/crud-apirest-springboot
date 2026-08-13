package com.lucassilva.api_rest.model;

import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import jakarta.persistence.*;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private double preco;

    @ManyToOne
    private Categoria categoria;

    public Produto(){

    }
    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(CadastroProdutoDTO cadastroProdutoDTO){
        this.nome = cadastroProdutoDTO.getNome();
        this.preco = cadastroProdutoDTO.getPreco();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
