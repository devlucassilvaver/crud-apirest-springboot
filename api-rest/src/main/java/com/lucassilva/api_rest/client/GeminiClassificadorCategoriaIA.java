package com.lucassilva.api_rest.client;

import com.lucassilva.api_rest.dto.response.CategoriaResumoResponseDTO;
import com.lucassilva.api_rest.dto.response.SugestaoCategoriaResponseDTO;
import java.util.List;
import com.lucassilva.api_rest.exception.FalhaIntegracaoIAException;
import com.lucassilva.api_rest.exception.RespostaIAInvalidaException;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class GeminiClassificadorCategoriaIA implements ClassificadorCategoriaIA{

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/interactions";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiClassificadorCategoriaIA(
            RestClient.Builder builder,
            ObjectMapper objectMapper,
            @Value("${GEMINI_API_KEY}") String apiKey) {

        this.objectMapper = objectMapper;

        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

        this.restClient = builder
                .baseUrl(GEMINI_URL)
                .requestFactory(requestFactory)
                .defaultHeader("x-goog-api-key", apiKey)
                .defaultHeader("Api-Revision", "2026-05-20")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public GeminiClassificadorCategoriaIA(
            RestClient restClient,
            ObjectMapper objectMapper) {

        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public SugestaoCategoriaResponseDTO sugerir(
            String nome,
            String descricao,
            List<CategoriaResumoResponseDTO> categorias){
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é um classificador de produtos.\n");
        prompt.append("Escolha exclusivamente uma das categorias disponíveis.\n");
        prompt.append("Se nenhuma categoria for adequada, retorne categoriaId null.\n\n");

        prompt.append("Produto: ").append(nome).append("\n");
        prompt.append("Descrição: ")
                .append(descricao == null ? "" : descricao)
                .append("\n\n");

        prompt.append("Categorias disponíveis:\n");

        for (CategoriaResumoResponseDTO categoria : categorias) {
            prompt.append(categoria.getId())
                    .append(" - ")
                    .append(categoria.getNome())
                    .append("\n");
        }

        Map<String, Object> propriedades = Map.of(
                "categoriaId", Map.of(
                        "type", List.of("integer", "null")
                ),
                "justificativa", Map.of(
                        "type", "string"
                )
        );

        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", propriedades,
                "required", List.of("categoriaId", "justificativa"),
                "additionalProperties", false
        );

        Map<String, Object> responseFormat = Map.of(
                "type", "text",
                "mime_type", "application/json",
                "schema", schema
        );

        Map<String, Object> corpoRequisicao = Map.of(
                "model", "gemini-3.5-flash-lite",
                "input", prompt.toString(),
                "response_format", responseFormat
        );

        ResponseEntity<String> resposta;

        try {
            resposta = restClient.post()
                    .body(corpoRequisicao)
                    .retrieve()
                    .toEntity(String.class);

        } catch (ResourceAccessException e) {
            throw new FalhaIntegracaoIAException(
                    "Não foi possível estabelecer comunicação com o Gemini.",
                    e
            );
        } catch (RestClientResponseException e) {
            throw new FalhaIntegracaoIAException(
                    "O Gemini retornou um erro HTTP: " + e.getStatusCode().value(),
                    e
            );
        }

        try {
            JsonNode respostaJson = objectMapper.readTree(resposta.getBody());

            JsonNode steps = respostaJson.path("steps");

            String textoGerado = null;

            for (JsonNode step : steps) {

                if ("model_output".equals(step.path("type").asText())) {

                    JsonNode conteudos = step.path("content");

                    for (JsonNode conteudo : conteudos) {

                        if ("text".equals(conteudo.path("type").asText())) {
                            textoGerado = conteudo.path("text").asText();
                            break;
                        }
                    }
                }

                if (textoGerado != null) {
                    break;
                }
            }

            if (textoGerado == null || textoGerado.isBlank()) {
                throw new RespostaIAInvalidaException(
                        "O Gemini não retornou um texto de classificação."
                );
            }

            return objectMapper.readValue(
                    textoGerado,
                    SugestaoCategoriaResponseDTO.class
            );

        } catch (JsonProcessingException e) {
            throw new RespostaIAInvalidaException(
                    "O Gemini retornou um JSON inválido."
            );
        }

    }
}
