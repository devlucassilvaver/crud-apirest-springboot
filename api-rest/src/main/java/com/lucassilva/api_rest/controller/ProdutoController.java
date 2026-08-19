package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.dto.request.AtualizacaoProdutoDTO;
import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.dto.response.ProdutoResponseDTO;
import com.lucassilva.api_rest.model.Produto;
import com.lucassilva.api_rest.service.ProdutoService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ProdutoResponseDTO> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId(@PathVariable int id){
        return produtoService.buscarProdutoResponsePorId(id);
    }

    @GetMapping("/categoria/{id}")
    public List<ProdutoResponseDTO> buscarProdutoPorCategoria(@PathVariable int id){
        return produtoService.buscarProdutoPorCategoria(id);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> adicionarProduto(@Valid @RequestBody CadastroProdutoDTO cadastroProdutoDTO){

        ProdutoResponseDTO produto = produtoService.adicionarProduto(cadastroProdutoDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produto);
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizarProduto(@PathVariable int id,
                                           @Valid @RequestBody AtualizacaoProdutoDTO atualizacaoProdutoDTO){

        ProdutoResponseDTO produto = produtoService.atualizarProduto(id, atualizacaoProdutoDTO);
        return produto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable int id){
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }
}
