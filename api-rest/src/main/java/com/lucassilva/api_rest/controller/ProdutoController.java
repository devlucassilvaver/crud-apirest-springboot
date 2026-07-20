package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public Produto buscarProdutoPorId(@PathVariable int id){
        return produtoService.buscarProdutoPorId(id);
    }

    @PostMapping
    public void adicionarProduto(@RequestBody Produto produto){
        produtoService.adicionarProduto(produto);
    }
}
