package com.lucassilva.api_rest.service;

import com.lucassilva.api_rest.client.ClassificadorCategoriaIA;
import com.lucassilva.api_rest.dto.request.SugestaoCategoriaRequestDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;
import com.lucassilva.api_rest.exception.RespostaIAInvalidaException;

public class SugestaoCategoriaService {

    private final CategoriaService categoriaService;
    private final ClassificadorCategoriaIA classificadorIA;

    public SugestaoCategoriaService(
            CategoriaService categoriaService,
            ClassificadorCategoriaIA classificadorIA
    ) {
        this.categoriaService = categoriaService;
        this.classificadorIA = classificadorIA;
    }

    public SugestaoCategoriaResponseDTO sugerir(
            SugestaoCategoriaRequestDTO solicitacao
    ) {
        var categorias = categoriaService.listarCategorias();

        if (categorias.isEmpty()) {
            return new SugestaoCategoriaResponseDTO(
                    null,
                    "Não há categorias cadastradas para sugerir."
            );
        }

        var resposta = classificadorIA.sugerir(
                solicitacao.nome(),
                solicitacao.descricao(),
                categorias
        );

        if (resposta == null
                || resposta.justificativa() == null
                || resposta.justificativa().isBlank()) {

            throw new RespostaIAInvalidaException(
                    "O modelo retornou uma resposta inválida."
            );
        }

        if (resposta.categoriaId() != null) {
            boolean categoriaDisponivel = categorias.stream()
                    .anyMatch(categoria ->
                            categoria.getId() == resposta.categoriaId()
                    );

            if (!categoriaDisponivel) {
                throw new RespostaIAInvalidaException(
                        "O modelo sugeriu uma categoria fora da lista disponível."
                );
            }
        }

        return resposta;
    }
}
