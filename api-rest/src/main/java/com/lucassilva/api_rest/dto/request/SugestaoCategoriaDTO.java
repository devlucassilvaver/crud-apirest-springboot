package com.lucassilva.api_rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SugestaoCategoriaDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        String descricao
) {
}
