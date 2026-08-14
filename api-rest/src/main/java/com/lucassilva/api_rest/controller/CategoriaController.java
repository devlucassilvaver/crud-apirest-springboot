package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.dto.request.CadastroCategoriaDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResponseDTO;
import com.lucassilva.api_rest.model.Categoria;
import com.lucassilva.api_rest.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Void> adicionarCategoria(
            @Valid @RequestBody CadastroCategoriaDTO cadastroCategoriaDTO){
        categoriaService.adicionarCategoria(cadastroCategoriaDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarCategoriaPorId(@PathVariable int id){
        return categoriaService.buscarCategoriaPorId(id);
    }
}
