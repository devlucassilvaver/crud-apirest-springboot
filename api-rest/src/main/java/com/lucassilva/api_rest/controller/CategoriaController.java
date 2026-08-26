package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.dto.request.AtualizacaoCategoriaDTO;
import com.lucassilva.api_rest.dto.request.CadastroCategoriaDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResponseDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResumoResponseDTO> listarCategorias(){
        return categoriaService.listarCategorias();
    }

    @PostMapping
    public ResponseEntity<CategoriaResumoResponseDTO> adicionarCategoria(
            @Valid @RequestBody CadastroCategoriaDTO cadastroCategoriaDTO){

        CategoriaResumoResponseDTO categoriaResumoResponseDTO =
                categoriaService.adicionarCategoria(cadastroCategoriaDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaResumoResponseDTO);
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarCategoriaPorId(@PathVariable int id){
        return categoriaService.buscarCategoriaPorId(id);
    }

    @PutMapping("/{id}")
    public CategoriaResumoResponseDTO atualizarCategoria(@PathVariable int id,
                                                         @Valid @RequestBody AtualizacaoCategoriaDTO atualizacaoCategoriaDTO){
        CategoriaResumoResponseDTO categoria = categoriaService.atualizarCategoria(id, atualizacaoCategoriaDTO);
        return categoria;

    }
}
