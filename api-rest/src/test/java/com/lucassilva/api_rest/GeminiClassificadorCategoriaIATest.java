package com.lucassilva.api_rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucassilva.api_rest.client.GeminiClassificadorCategoriaIA;
import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;
import com.lucassilva.api_rest.exception.FalhaIntegracaoIAException;
import com.lucassilva.api_rest.exception.RespostaIAInvalidaException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.springframework.web.client.ResourceAccessException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;


class GeminiClassificadorCategoriaIATest {
    private MockRestServiceServer servidorSimulado;
    private GeminiClassificadorCategoriaIA classificador;

    @BeforeEach
    void configurar() {
        RestClient.Builder builder = RestClient.builder();

        servidorSimulado = MockRestServiceServer.bindTo(builder).build();

        RestClient cliente = builder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/interactions")
                .build();

        classificador = new GeminiClassificadorCategoriaIA(
                cliente,
                new ObjectMapper()
        );
    }

    @Test
    void deveLancarExcecaoQuandoGeminiRetornar503() {

        servidorSimulado.expect(
                        requestTo("https://generativelanguage.googleapis.com/v1beta/interactions")
                )
                .andExpect(method(POST))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        List<CategoriaResumoResponseDTO> categorias = List.of(
                new CategoriaResumoResponseDTO(5, "Periféricos")
        );

        assertThrows(
                FalhaIntegracaoIAException.class,
                () -> classificador.sugerir(
                        "Mouse Gamer",
                        "Mouse USB",
                        categorias
                )
        );

        servidorSimulado.verify();
    }

    @Test
    void deveLancarExcecaoQuandoGeminiRetornarJsonMalformado() {

        servidorSimulado.expect(
                        requestTo("https://generativelanguage.googleapis.com/v1beta/interactions")
                )
                .andExpect(method(POST))
                .andRespond(
                        withSuccess(
                                "{ json invalido",
                                MediaType.APPLICATION_JSON
                        )
                );

        List<CategoriaResumoResponseDTO> categorias = List.of(
                new CategoriaResumoResponseDTO(5, "Periféricos")
        );

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> classificador.sugerir(
                        "Mouse Gamer",
                        "Mouse USB",
                        categorias
                )
        );

        servidorSimulado.verify();
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverModelOutput() {

        String respostaGemini = """
            {
              "status": "completed",
              "steps": [
                {
                  "type": "thought"
                }
              ]
            }
            """;

        servidorSimulado.expect(
                        requestTo("https://generativelanguage.googleapis.com/v1beta/interactions")
                )
                .andExpect(method(POST))
                .andRespond(
                        withSuccess(respostaGemini, MediaType.APPLICATION_JSON)
                );

        List<CategoriaResumoResponseDTO> categorias = List.of(
                new CategoriaResumoResponseDTO(5, "Periféricos")
        );

        assertThrows(
                RespostaIAInvalidaException.class,
                () -> classificador.sugerir(
                        "Mouse Gamer",
                        "Mouse USB",
                        categorias
                )
        );

        servidorSimulado.verify();
    }

    @Test
    void deveRetornarCategoriaQuandoGeminiResponderCorretamente() {

        String respostaGemini = """
            {
              "status": "completed",
              "steps": [
                {
                  "type": "model_output",
                  "content": [
                    {
                      "type": "text",
                      "text": "{\\"categoriaId\\":5,\\"justificativa\\":\\"Produto de informática.\\"}"
                    }
                  ]
                }
              ]
            }
            """;

        servidorSimulado.expect(
                        requestTo("https://generativelanguage.googleapis.com/v1beta/interactions")
                )
                .andExpect(method(POST))
                .andRespond(
                        withSuccess(respostaGemini, MediaType.APPLICATION_JSON)
                );

        List<CategoriaResumoResponseDTO> categorias = List.of(
                new CategoriaResumoResponseDTO(5, "Periféricos")
        );

        SugestaoCategoriaResponseDTO resultado = classificador.sugerir(
                "Mouse Gamer",
                "Mouse USB",
                categorias
        );

        assertEquals(5, resultado.categoriaId());
        assertEquals("Produto de informática.", resultado.justificativa());

        servidorSimulado.verify();
    }

    @Test
    void deveLancarExcecaoQuandoOcorrerFalhaDeConexao() {

        servidorSimulado.expect(
                        requestTo("https://generativelanguage.googleapis.com/v1beta/interactions")
                )
                .andExpect(method(POST))
                .andRespond(
                        withException(new IOException("Falha de conexão simulada"))
                );

        List<CategoriaResumoResponseDTO> categorias = List.of(
                new CategoriaResumoResponseDTO(5, "Periféricos")
        );

        assertThrows(
                FalhaIntegracaoIAException.class,
                () -> classificador.sugerir(
                        "Mouse Gamer",
                        "Mouse USB",
                        categorias
                )
        );

        servidorSimulado.verify();
    }

}