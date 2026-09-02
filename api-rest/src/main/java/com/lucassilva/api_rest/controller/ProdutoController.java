package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.dto.request.AtualizacaoProdutoDTO;
import com.lucassilva.api_rest.dto.request.CadastroProdutoDTO;
import com.lucassilva.api_rest.dto.response.ProdutoResponseDTO;
import com.lucassilva.api_rest.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public Page<ProdutoResponseDTO> listarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer categoriaId,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        if (categoriaId != null){
            return produtoService.buscarProdutoPorCategoria(categoriaId, pageable);
        } else if(nome != null){
            return produtoService.buscarProdutoPorNome(nome, pageable);
        } else {
            return produtoService.listarProdutos(pageable);
        }
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId(@PathVariable int id){
        return produtoService.buscarProdutoResponsePorId(id);
    }

    @GetMapping("/categoria/{id}")
    public Page<ProdutoResponseDTO> buscarProdutoPorCategoria(
            @PathVariable int id,
            Pageable pageable){
        return produtoService.buscarProdutoPorCategoria(id, pageable);
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
