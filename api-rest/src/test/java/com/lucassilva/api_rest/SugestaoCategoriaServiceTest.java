package com.lucassilva.api_rest;

import com.lucassilva.api_rest.client.ClassificadorCategoriaIA;
import com.lucassilva.api_rest.dto.request.SugestaoCategoriaRequestDTO;
import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;
import com.lucassilva.api_rest.exception.RespostaIAInvalidaException;
import com.lucassilva.api_rest.service.CategoriaService;
import com.lucassilva.api_rest.service.SugestaoCategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lucassilva.api_rest.exception.FalhaIntegracaoIAException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SugestaoCategoriaServiceTest {

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private ClassificadorCategoriaIA classificadorIA;

    @InjectMocks
    private SugestaoCategoriaService sugestaoCategoriaService;

    private final SugestaoCategoriaRequestDTO solicitacaoPadrao =
            new SugestaoCategoriaRequestDTO(
                    "Mouse",
                    "Mouse USB para computador"
            );

    private final List<CategoriaResumoResponseDTO> categoriasPadrao =
            List.of(
                    new CategoriaResumoResponseDTO(7, "Eletrônicos")
            );
    @Test
    void deveRetornarCategoriaDisponivel() {
        // Arrange: preparar os dados e as respostas simuladas.
        var solicitacao = new SugestaoCategoriaRequestDTO(
                "Mouse",
                "Mouse USB para computador"
        );

        var categorias = List.of(
                new CategoriaResumoResponseDTO(7, "Eletrônicos")
        );

        var respostaIA = new SugestaoCategoriaResponseDTO(
                7,
                "O produto é um periférico de computador."
        );

        when(categoriaService.listarCategorias())
                .thenReturn(categorias);

        when(classificadorIA.sugerir(
                solicitacao.nome(),
                solicitacao.descricao(),
                categorias
        )).thenReturn(respostaIA);

        // Act: executar o método que estamos testando.
        var resultado = sugestaoCategoriaService.sugerir(solicitacao);

        // Assert: conferir o resultado.
        assertEquals(Integer.valueOf(7), resultado.categoriaId());

        assertEquals(
                "O produto é um periférico de computador.",
                resultado.justificativa()
        );
    }

    @Test
    void deveRejeitarCategoriaForaDaLista() {
        var solicitacao = new SugestaoCategoriaRequestDTO(
                "Mouse",
                "Mouse USB para computador"
        );

        var categorias = List.of(
                new CategoriaResumoResponseDTO(7, "Eletrônicos")
        );

        when(categoriaService.listarCategorias())
                .thenReturn(categorias);

        when(classificadorIA.sugerir(
                solicitacao.nome(),
                solicitacao.descricao(),
                categorias
        )).thenReturn(
                new SugestaoCategoriaResponseDTO(
                        999,
                        "Categoria sugerida pelo modelo."
                )
        );

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> sugestaoCategoriaService.sugerir(solicitacao)
        );
    }

    @Test
    void deveEvitarChamadaQuandoNaoExistiremCategorias() {
        var solicitacao = new SugestaoCategoriaRequestDTO(
                "Mouse",
                "Mouse USB para computador"
        );

        when(categoriaService.listarCategorias())
                .thenReturn(List.of());

        var resultado = sugestaoCategoriaService.sugerir(solicitacao);

        assertNull(resultado.categoriaId());

        verifyNoInteractions(classificadorIA);
    }

    @Test
    void deveRejeitarRespostaNula() {
        when(categoriaService.listarCategorias())
                .thenReturn(categoriasPadrao);

        when(classificadorIA.sugerir(
                solicitacaoPadrao.nome(),
                solicitacaoPadrao.descricao(),
                categoriasPadrao
        )).thenReturn(null);

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> sugestaoCategoriaService.sugerir(solicitacaoPadrao)
        );
    }

    @Test
    void deveRejeitarJustificativaNula() {
        when(categoriaService.listarCategorias())
                .thenReturn(categoriasPadrao);

        when(classificadorIA.sugerir(
                solicitacaoPadrao.nome(),
                solicitacaoPadrao.descricao(),
                categoriasPadrao
        )).thenReturn(
                new SugestaoCategoriaResponseDTO(7, null)
        );

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> sugestaoCategoriaService.sugerir(solicitacaoPadrao)
        );
    }

    @Test
    void deveRejeitarJustificativaVazia() {
        when(categoriaService.listarCategorias())
                .thenReturn(categoriasPadrao);

        when(classificadorIA.sugerir(
                solicitacaoPadrao.nome(),
                solicitacaoPadrao.descricao(),
                categoriasPadrao
        )).thenReturn(
                new SugestaoCategoriaResponseDTO(7, "")
        );

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> sugestaoCategoriaService.sugerir(solicitacaoPadrao)
        );
    }

    @Test
    void deveAceitarAusenciaDeSugestaoComJustificativa() {
        when(categoriaService.listarCategorias())
                .thenReturn(categoriasPadrao);

        when(classificadorIA.sugerir(
                solicitacaoPadrao.nome(),
                solicitacaoPadrao.descricao(),
                categoriasPadrao
        )).thenReturn(
                new SugestaoCategoriaResponseDTO(
                        null,
                        "As informações não permitem sugerir uma categoria."
                )
        );

        var resultado = sugestaoCategoriaService.sugerir(
                solicitacaoPadrao
        );

        assertNull(resultado.categoriaId());

        assertEquals(
                "As informações não permitem sugerir uma categoria.",
                resultado.justificativa()
        );
    }

    @Test
    void devePropagarFalhaDeIntegracao() {
        when(categoriaService.listarCategorias())
                .thenReturn(categoriasPadrao);

        when(classificadorIA.sugerir(
                solicitacaoPadrao.nome(),
                solicitacaoPadrao.descricao(),
                categoriasPadrao
        )).thenThrow(
                new FalhaIntegracaoIAException(
                        "Não foi possível obter uma resposta do provedor."
                )
        );

        assertThrows(
                FalhaIntegracaoIAException.class,
                () -> sugestaoCategoriaService.sugerir(solicitacaoPadrao)
        );
    }
}