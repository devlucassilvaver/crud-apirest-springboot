package com.lucassilva.api_rest.client;

import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaDTO;

import java.util.List;

public interface ClassificadorCategoriaIA {
    SugestaoCategoriaDTO sugerir(
            String nome,
            String descricao,
            List<CategoriaResumoResponseDTO> categorias
    );
}
