package com.lucassilva.api_rest.service;


import com.lucassilva.api_rest.exception.ProdutoNaoEncontradoException;
import com.lucassilva.api_rest.model.Produto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {

    private List<Produto> produtos = new ArrayList<>();

    public List<Produto> listarProdutos(){
        return produtos;
    }

    public Produto buscarProdutoPorId(int id){
        for (Produto produto : produtos){
            if (produto.getId() == id){
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException(
              "Produto com id " + id + " não encontrado."
        );
    }

    public void adicionarProduto(Produto produto){
        produtos.add(produto);
    }

    public void atualizarProduto(int id, Produto produto){
        Produto produtoEncontrado = buscarProdutoPorId(id);
        produtoEncontrado.setNome(produto.getNome());
        produtoEncontrado.setPreco(produto.getPreco());
    }

    public void removerProduto(int id){
        Produto produtoEncontrato = buscarProdutoPorId(id);
        produtos.remove(produtoEncontrato);
    }
}
