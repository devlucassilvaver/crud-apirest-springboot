# Produtos e Categorias — API REST com IA Generativa

API REST desenvolvida em Java 21 e Spring Boot para gerenciamento de produtos e categorias, com integração ao Google Gemini para sugerir classificações com base nas categorias cadastradas no MySQL.

A IA atua como apoio à decisão: retorna uma sugestão e sua justificativa. A confirmação da categoria e o cadastro do produto continuam sob responsabilidade do usuário.

## Tecnologias

- Java 21 e Spring Boot 3.5.6
- Spring Web e RestClient
- Spring Data JPA e Hibernate
- MySQL
- Bean Validation e Jackson
- Google Gemini API
- JUnit 5, Mockito e MockRestServiceServer
- Maven

## Funcionalidades

- CRUD de produtos e categorias.
- Associação de produtos a categorias.
- Pesquisa de produtos por nome ou categoria.
- Paginação e ordenação de produtos.
- Validação dos dados de entrada.
- Tratamento centralizado de exceções.
- Bloqueio da exclusão de categorias com produtos associados.
- Sugestão de categoria com IA generativa.
- Validação da sugestão contra o catálogo consultado.
- Tratamento de falhas HTTP, comunicação e respostas inválidas do provedor.

Os filtros por nome e categoria não podem ser utilizados simultaneamente.

## Arquitetura

A aplicação separa as responsabilidades entre:

- **Controller:** recebe requisições HTTP e aplica validação de entrada.
- **Service:** executa regras de negócio.
- **Repository:** acessa os dados com Spring Data JPA.
- **Client:** realiza a integração HTTP com o Gemini.
- **DTO:** define os dados de entrada e saída.
- **Handler:** padroniza as respostas de erro.

O serviço de sugestão depende da interface `ClassificadorCategoriaIA`.
A implementação `GeminiClassificadorCategoriaIA` concentra a comunicação com o provedor.

## Integração com IA

1. O cliente envia o nome e, opcionalmente, a descrição do produto.
2. A aplicação consulta as categorias cadastradas.
3. Se o catálogo estiver vazio, retorna ausência de sugestão sem chamar a IA.
4. O cliente Gemini envia os dados do produto e as categorias disponíveis.
5. A resposta estruturada é extraída e convertida com Jackson.
6. O serviço valida a justificativa e a existência da categoria no catálogo consultado.
7. A sugestão é retornada sem cadastrar ou alterar produtos.

Configuração da integração:

- Endpoint: `POST https://generativelanguage.googleapis.com/v1beta/interactions`
- Modelo configurado: `gemini-3.5-flash-lite`
- Revisão da API: `2026-05-20`
- Timeout de conexão: 5 segundos
- Timeout de leitura: 30 segundos

Os timeouts de conexão e leitura não representam um limite total de duração da operação. A disponibilidade do modelo depende do provedor e da conta utilizada.

## Endpoints

### Produtos

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/produtos` | Lista produtos com paginação, ordenação e filtro opcional |
| GET | `/produtos/{id}` | Consulta um produto |
| GET | `/produtos/categoria/{id}` | Lista produtos de uma categoria |
| POST | `/produtos` | Cadastra um produto |
| PUT | `/produtos/{id}` | Atualiza um produto |
| DELETE | `/produtos/{id}` | Exclui um produto |
| POST | `/produtos/sugestao-categoria` | Solicita uma sugestão de categoria à IA |

Exemplos de consulta:

```http
GET /produtos?page=0&size=10&sort=id,asc
GET /produtos?nome=Mouse
GET /produtos?categoriaId=5
```

Em `GET /produtos`, os padrões são página 0, tamanho 10 e ordenação por ID crescente.

### Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/categorias` | Lista categorias |
| GET | `/categorias/{id}` | Consulta uma categoria |
| POST | `/categorias` | Cadastra uma categoria |
| PUT | `/categorias/{id}` | Atualiza uma categoria |
| DELETE | `/categorias/{id}` | Exclui uma categoria sem produtos associados |

Cadastros retornam HTTP 201. Exclusões bem-sucedidas retornam HTTP 204.

## Exemplo de sugestão

```http
POST /produtos/sugestao-categoria
Content-Type: application/json
```

```json
{
  "nome": "Mouse Gamer",
  "descricao": "Mouse USB com iluminação RGB e sensor óptico."
}
```

Resposta ilustrativa, considerando uma categoria compatível com ID 5 no catálogo:

```json
{
  "categoriaId": 5,
  "justificativa": "O produto é um periférico de computador."
}
```

O modelo pode retornar ausência de sugestão:

```json
{
  "categoriaId": null,
  "justificativa": "As informações não permitem sugerir uma categoria."
}
```

O nome é obrigatório. A descrição é opcional. As respostas do modelo podem variar.

## Configuração e execução

### Pré-requisitos

- JDK 21.
- MySQL em execução.
- Banco de dados criado.
- Chave de acesso ao Gemini e disponibilidade do modelo configurado.

Crie o banco, caso ainda não exista:

```sql
CREATE DATABASE IF NOT EXISTS api_rest;
```

Configure as variáveis no ambiente local:

| Variável | Finalidade |
|---|---|
| `DB_URL` | URL JDBC, como `jdbc:mysql://localhost:3306/api_rest` |
| `DB_USERNAME` | Usuário do MySQL |
| `DB_PASSWORD` | Senha do MySQL |
| `GEMINI_API_KEY` | Chave de acesso ao Gemini |

Não versione senhas ou chaves.

Após clonar o repositório, entre na pasta da aplicação:

```powershell
cd crud-apirest-springboot
cd api-rest
```

Dentro dessa pasta, inicie a aplicação:

```powershell
.\mvnw.cmd spring-boot:run
```

Sem alteração da porta padrão, a API fica disponível em:

```text
http://localhost:8080
```

Variáveis definidas com `$env:` valem somente para a sessão atual do PowerShell.
A execução pelo botão Run do IntelliJ utiliza a configuração de ambiente daquela execução.

A configuração atual utiliza `spring.jpa.hibernate.ddl-auto: update`, que permite alterações automáticas no esquema. Ela não substitui migrações controladas ou backups.

## Testes

Executar a suíte completa:

```powershell
.\mvnw.cmd test
```

Executar somente os testes do cliente Gemini:

```powershell
.\mvnw.cmd "-Dtest=GeminiClassificadorCategoriaIATest" test
```

Resultado da última execução local validada:

**23 testes executados, sem falhas, erros ou testes ignorados.**

A suíte inclui 7 cenários HTTP simulados do cliente Gemini:

- HTTP 503.
- JSON externo malformado.
- Ausência de `model_output`.
- Resposta válida.
- Falha de comunicação.
- Corpo HTTP vazio.
- JSON interno malformado.

Esses testes utilizam `MockRestServiceServer` e não fazem chamadas reais ao Gemini.

Os testes do serviço também verificam respostas inválidas, categoria fora do catálogo, catálogo vazio e ausência válida de sugestão.

O teste `ApiRestApplicationTests.contextLoads` carrega o contexto Spring e, na configuração atual, depende do MySQL e das variáveis de ambiente, incluindo `GEMINI_API_KEY`.

## Erros HTTP

| Status | Situação |
|---|---|
| 400 Bad Request | Falha de validação ou uso simultâneo dos filtros de nome e categoria |
| 404 Not Found | Produto ou categoria não encontrado |
| 409 Conflict | Tentativa de excluir categoria com produtos associados |
| 502 Bad Gateway | Falha de integração ou resposta inválida da IA |
| 500 Internal Server Error | Erro inesperado da aplicação |

## Limitações e próximos passos

- A qualidade das classificações ainda não foi avaliada sistematicamente com produtos fictícios.
- Os testes simulados não medem a precisão do modelo nem comprovam os timeouts em uma conexão real.
- As respostas dependem da qualidade das informações do produto e do catálogo disponível.
- Ainda há melhorias previstas na validação estrutural do JSON retornado pelo provedor.
- A configuração dos testes de contexto ainda depende de recursos locais.
- A IA pode sugerir uma categoria inadequada; a revisão humana continua necessária.