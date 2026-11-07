package com.lucassilva.api_rest.controller;

import com.lucassilva.api_rest.dto.request.SugestaoCategoriaRequestDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;
import com.lucassilva.api_rest.service.SugestaoCategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos/sugestao-categoria")
public class SugestaoCategoriaController {

    private final SugestaoCategoriaService sugestaoCategoriaService;

    public SugestaoCategoriaController(
            SugestaoCategoriaService sugestaoCategoriaService) {

        this.sugestaoCategoriaService = sugestaoCategoriaService;
    }

    @PostMapping
    public ResponseEntity<SugestaoCategoriaResponseDTO> sugerir(
            @Valid @RequestBody SugestaoCategoriaRequestDTO solicitacao) {

        SugestaoCategoriaResponseDTO resposta =
                sugestaoCategoriaService.sugerir(solicitacao);

        return ResponseEntity.ok(resposta);
    }
}