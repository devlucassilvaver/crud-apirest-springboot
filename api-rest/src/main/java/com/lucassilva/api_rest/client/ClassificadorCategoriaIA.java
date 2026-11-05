package com.lucassilva.api_rest.client;

import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;

import java.util.List;

public interface ClassificadorCategoriaIA {
    SugestaoCategoriaResponseDTO sugerir(
            String nome,
            String descricao,
            List<CategoriaResumoResponseDTO> categorias
    );
}
