# Golden Raspberry Awards API

API RESTful para consulta dos indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

## Arquitetura

O projeto foi desenvolvido seguindo os princípios de **Domain Driven Design (DDD)** e  **Clean Code** com a seguinte estrutura:

```
src/main/java/br/com/asneu/award/
├── domain/             # Entidades e contratos
├── infrastructure/     # Implementações JPA
├── application/        # Serviços de aplicação
└── presentation/       # REST controllers, DTOs e exceções
```

## Tecnologias Utilizadas

- **Spring Boot 3.2.5**
- **Java 21**
- **Spring Data JPA**
- **H2 Database**
- **OpenCSV** para leitura do arquivo CSV
- **SpringDoc OpenAPI 2.2.0** (Swagger) para documentação
- **Gradle** como ferramenta de build
- **JUnit 5** para testes de integração

## Pré-requisitos

- Java 21 ou superior
- Gradle (ou usar o wrapper incluído)

## Instalação e Execução

### 1. Clone o repositório
```
git clone https://github.com/andresnjr/poc-award.git
cd award
```

### 2. Executar a aplicação
```
# Windows
./gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

A aplicação será iniciada na porta **8080**.

### 3. Verificar se está funcionando
Acesse: http://localhost:8080/api/producers/prize-intervals

### 4. Documentação da API (Swagger)
Acesse: http://localhost:8080/swagger-ui.html

## Endpoints da API

A API possui documentação completa disponível via **Swagger UI** em: http://localhost:8080/swagger-ui.html

### GET `/api/producers/prize-intervals`
Retorna os produtores com maior e menor intervalo entre dois prêmios consecutivos.

**Resposta:**
```json
{
  "min": [
    {
      "producer": "Producer 1",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    }
  ],
  "max": [
    {
      "producer": "Producer 2", 
      "interval": 99,
      "previousWin": 1900,
      "followingWin": 1999
    }
  ]
}
```

### Documentação OpenAPI
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Banco de Dados

A aplicação utiliza **H2** em memória. Os dados são carregados automaticamente do arquivo `src/main/resources/movielist.csv` na inicialização.

### Acesso ao Banco H2

**Console Web H2:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `sa`

**Conexão Externa (DBeaver/IntelliJ/etc):**
- Host: `localhost`
- Porta: `9092`
- JDBC URL: `jdbc:h2:tcp://localhost:9092/mem:testdb`
- Username: `sa`
- Password: `sa`
- Driver: H2 Database

## Executar Testes

### Testes de Integração
```
# Windows  
./gradlew.bat test

# Linux/Mac
./gradlew test
```

Os testes de integração verificam:
- Importação correta dos dados do CSV
- Funcionamento do endpoint de intervalos
- Integridade dos dados
- Estrutura correta da resposta JSON

### Executar testes específicos
```
# Teste de importação CSV
./gradlew test --tests "*CsvImportIntegrationTest"

# Teste do controller
./gradlew test --tests "*ProducerControllerIntegrationTest"
```

## Arquivo CSV

O arquivo `movielist.csv` deve estar em `src/main/resources/` com o seguinte formato:
```csv
year;title;studios;producers;winner
1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes
1981;Mommie Dearest;Paramount Pictures;Frank Yablans;yes
```

**Colunas:**
- `year`: Ano do filme
- `title`: Título do filme  
- `studios`: Estúdios produtores
- `producers`: Produtores (separados por vírgula ou "and")
- `winner`: "yes" para vencedores, vazio ou "no" para indicados

## Build para Produção

```
# Gerar WAR executável
./gradlew build

# WAR será gerado em: build/libs/award-0.0.1-SNAPSHOT.war
```

## Características Técnicas

### Nível de Maturidade Richardson - Nível 2
- Uso de HTTP verbs (GET)
- URIs como identificadores de recursos
- Códigos de status HTTP apropriados
- Representação JSON

### Clean Code
- Nomes descritivos para classes, métodos e variáveis
- Métodos pequenos e com responsabilidade única
- Comentários apenas quando necessário
- Tratamento adequado de exceções

## Troubleshooting

### Porta já em uso
Se a porta 8080 estiver ocupada, altere em `application.yml`:
```yaml
server:
  port: 8081
```

### Problemas com arquivo CSV
Verifique se o arquivo `movielist.csv` existe em `src/main/resources/static` e se está no formato correto com separador `;`.

### Falha nos testes
Execute com logs detalhados:
```
./gradlew test --info
```