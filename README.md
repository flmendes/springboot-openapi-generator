# Spring Boot OpenAPI Generator

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-blue.svg)](https://www.openapis.org/)
[![RFC 7807](https://img.shields.io/badge/RFC%207807-Problem%20Details-success.svg)](https://tools.ietf.org/html/rfc7807)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Projeto de exemplo demonstrando as melhores práticas para desenvolvimento de APIs REST com Spring Boot, incluindo:

- 🚀 **Spring Boot 3.5.7** com Java 21
- 📝 **OpenAPI Generator** para desenvolvimento API-first
- 🔒 **RFC 7807 Problem Details** para respostas de erro padronizadas
- 📚 **Javadoc completo** em todas as classes
- 🐳 **Cloud Native Buildpacks** para containerização
- 🔄 **CI/CD com GitHub Actions** automatizado

## 📋 Índice

- [Características](#-características)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Executando o Projeto](#-executando-o-projeto)
- [API Endpoints](#-api-endpoints)
- [Docker](#-docker)
- [CI/CD](#-cicd)
- [Documentação](#-documentação)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Contribuindo](#-contribuindo)

## ✨ Características

### Desenvolvimento API-First com OpenAPI

- Especificação OpenAPI 3.0 em `src/main/resources/openapi/student.yml`
- Geração automática de interfaces e models com OpenAPI Generator
- Contratos de API versionados e documentados

### RFC 7807 Problem Details

Todas as respostas de erro seguem o padrão RFC 7807, fornecendo:

```json
{
  "type": "https://api.example.com/errors/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed for one or more fields",
  "instance": "/students",
  "errors": "email: must be a valid email"
}
```

### Javadoc Completo

Todas as classes possuem documentação Javadoc profissional:

- Classes documentadas com propósito e responsabilidades
- Métodos com `@param`, `@return`, `@throws`
- Links cruzados com `@see`
- Geração de documentação HTML

### Cloud Native Buildpacks

Containerização sem Dockerfile usando Spring Boot Buildpacks:

- JRE-only runtime otimizado
- Imagens reproduzíveis e seguras
- Layer caching para builds rápidos
- Base images mantidas pela Paketo

## 🛠 Tecnologias

- **Java 21** - LTS com recursos modernos
- **Spring Boot 3.5.7** - Framework principal
- **Spring Web** - REST API
- **Spring Validation** - Validação JSR-303/JSR-380
- **Spring Actuator** - Health checks e métricas
- **OpenAPI Generator 7.14.0** - Geração de código a partir de OpenAPI
- **Maven** - Gerenciamento de dependências e build
- **Paketo Buildpacks** - Cloud Native containerização
- **JUnit 5** - Testes unitários e integração
- **Docker** - Containerização

## 📦 Pré-requisitos

- Java 21 ou superior
- Maven 3.9+ ou usar o Maven Wrapper incluído (`./mvnw`)
- Docker (opcional, para executar containers)
- Git

## 🚀 Instalação

1. **Clone o repositório**

```bash
git clone https://github.com/<flmendes>/springboot-openapi-generator.git
cd springboot-openapi-generator
```

2. **Compile o projeto**

```bash
./mvnw clean install
```

O OpenAPI Generator irá automaticamente gerar as interfaces e models durante o build.

## ▶️ Executando o Projeto

### Execução Local

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Com Docker (Buildpacks)

#### Imagem JVM (padrão)

```bash
# Build da imagem JVM
./mvnw spring-boot:build-image

# Run do container
docker run -p 8080:8080 springboot-openapi-generator:0.0.1-SNAPSHOT
```

#### Imagem Nativa (GraalVM)

```bash
# Build da imagem nativa com GraalVM
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.env.BP_NATIVE_IMAGE=true

# Run do container nativo (startup instantâneo)
docker run -p 8080:8080 springboot-openapi-generator:0.0.1-SNAPSHOT
```

**Vantagens da Imagem Nativa:**
- ⚡ Startup instantâneo (< 100ms vs ~2s JVM)
- 💾 Menor consumo de memória (~50MB vs ~200MB)
- 📦 Imagem menor (~80MB vs ~224MB)

### Verificar Health

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:
```json
{"status":"UP"}
```

## 🌐 API Endpoints

### POST /students

Cria um novo estudante.

**Request:**

```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "(11) 99999-9999"
  }'
```

**Response (201 Created):**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "(11) 99999-9999"
}
```

**Validações:**

- `name`: obrigatório
- `email`: obrigatório e formato válido
- `phone`: obrigatório

**Respostas de Erro:**

- `400 Bad Request` - Dados inválidos (RFC 7807)
- `409 Conflict` - Email já existe (RFC 7807)

### GET /actuator/health

Health check do Spring Boot Actuator.

```bash
curl http://localhost:8080/actuator/health
```

## 🐳 Docker

### Build com Spring Boot Buildpacks

#### Imagem JVM

```bash
# Build padrão (JVM)
./mvnw spring-boot:build-image

# Build com nome customizado
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.imageName=myapp:1.0
```

#### Imagem Nativa

```bash
# Build com GraalVM Native Image
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.env.BP_NATIVE_IMAGE=true

# Build nativa com nome customizado
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.imageName=myapp:1.0-native \
  -Dspring-boot.build-image.env.BP_NATIVE_IMAGE=true
```

### Executar Container

```bash
# Run imagem JVM em background
docker run -d -p 8080:8080 --name my-app \
  springboot-openapi-generator:0.0.1-SNAPSHOT

# Run imagem nativa (startup instantâneo)
docker run -d -p 8080:8080 --name my-app-native \
  springboot-openapi-generator:0.0.1-SNAPSHOT

# Ver logs
docker logs -f my-app

# Parar container
docker stop my-app
docker rm my-app
```

### Imagens Docker

#### Imagem JVM
- **Builder**: `paketobuildpacks/builder-noble-java-tiny`
- **Base Runtime**: Ubuntu Noble Tiny
- **JRE**: BellSoft Liberica 21
- **Size**: ~224 MB
- **Startup**: ~2 segundos
- **Memory**: ~200 MB
- **User**: CNB (non-root, UID 1000)

#### Imagem Nativa (GraalVM)
- **Builder**: `paketobuildpacks/builder-noble-tiny` (com Native Image buildpack)
- **Base Runtime**: Ubuntu Noble Tiny
- **Runtime**: GraalVM Native Image
- **Size**: ~80 MB
- **Startup**: < 100 ms
- **Memory**: ~50 MB
- **User**: CNB (non-root, UID 1000)

## 🔄 CI/CD

O projeto inclui um pipeline completo de CI/CD com GitHub Actions.

### Pipeline

```
Test → Build → Build Image (JVM) + Build Image (Native) → Push to Registry → Summary
```

**Jobs:**

1. **Test** - Executa todos os testes (`./mvnw test`)
2. **Build** - Compila e cria o JAR
3. **Build & Push Image (JVM)** - Cria imagem JVM com Buildpacks e faz push (paralelo)
4. **Build & Push Image (Native)** - Cria imagem nativa com GraalVM Native Image (paralelo)
5. **Summary** - Gera relatório de deployment

### Triggers

- Push para `main` ou `develop`
- Pull Requests
- Manual (workflow_dispatch)

### Docker Registry

Imagens são publicadas em GitHub Container Registry:

#### Imagens JVM

```bash
# Latest (main branch)
docker pull ghcr.io/<username>/springboot-openapi-generator:latest

# Branch específico
docker pull ghcr.io/<username>/springboot-openapi-generator:main
```

#### Imagens Nativas

```bash
# Latest nativa (main branch)
docker pull ghcr.io/<username>/springboot-openapi-generator:latest-native

# Branch específico nativa
docker pull ghcr.io/<username>/springboot-openapi-generator:main-native
```

### Mais Informações

Veja [CI-CD.md](CI-CD.md) para documentação completa do pipeline.

## 📚 Documentação

### Javadoc

Gerar documentação Javadoc:

```bash
./mvnw javadoc:javadoc
```

A documentação será gerada em `target/reports/apidocs/index.html`

### OpenAPI Specification

A especificação da API está em:

```
src/main/resources/openapi/student.yml
```

### Documentação Adicional

- [CI-CD.md](CI-CD.md) - Pipeline de CI/CD completo
- [HELP.md](HELP.md) - Guia de referência Spring Boot

## 🧪 Testes

### Executar Todos os Testes

```bash
./mvnw test
```

### Testes Incluídos

- **Unit Tests**: 12 testes
  - Testes de controller com MockMvc
  - Testes de service
  - Testes de model (Java Records)

### Cobertura de Testes

- `StudentControllerTest` - 8 testes
  - Criação bem-sucedida
  - Validações (nome, email, phone)
  - Email duplicado (409)
  - JSON inválido

- `StudentServiceTest` - Testes de lógica de negócio
- `SpringbootOpenapiGeneratorApplicationTests` - Context load

### Relatórios de Teste

Após executar os testes, os relatórios estão em:

```
target/surefire-reports/
```

## 📁 Estrutura do Projeto

```
springboot-openapi-generator/
├── .github/
│   └── workflows/
│       └── ci-cd.yml              # Pipeline GitHub Actions
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Application.java           # Classe principal
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java # REST Controller
│   │   │   ├── service/
│   │   │   │   └── StudentService.java    # Lógica de negócio
│   │   │   ├── model/
│   │   │   │   └── Student.java           # Domain model (Record)
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java  # RFC 7807
│   │   │       └── EmailAlreadyExistsException.java
│   │   └── resources/
│   │       ├── application.properties     # Configurações
│   │       └── openapi/
│   │           └── student.yml            # OpenAPI Spec
│   └── test/
│       └── java/com/example/
│           ├── controller/
│           │   └── StudentControllerTest.java
│           └── service/
│               └── StudentServiceTest.java
├── target/
│   └── generated-sources/
│       └── openapi/                       # Código gerado
├── pom.xml                                # Maven configuration
├── README.md                              # Este arquivo
├── CI-CD.md                               # Documentação CI/CD
└── HELP.md                                # Spring Boot Guide
```

## 🎯 Boas Práticas Implementadas

### Arquitetura

- ✅ **API-First Development** - Contrato definido antes da implementação
- ✅ **Separation of Concerns** - Controller, Service, Model bem definidos
- ✅ **Immutability** - Uso de Java Records
- ✅ **Dependency Injection** - Constructor injection

### Qualidade de Código

- ✅ **Javadoc completo** - Todas as classes documentadas
- ✅ **RFC 7807** - Respostas de erro padronizadas
- ✅ **Validation** - JSR-303 Bean Validation
- ✅ **Exception Handling** - Global exception handler

### DevOps

- ✅ **Cloud Native Buildpacks** - Containerização moderna
- ✅ **CI/CD** - Pipeline automatizado
- ✅ **Health Checks** - Spring Actuator
- ✅ **Tests** - Cobertura de testes adequada

### Segurança

- ✅ **Non-root containers** - Containers executam como usuário não privilegiado
- ✅ **Minimal images** - JRE-only runtime
- ✅ **Updated dependencies** - Spring Boot 3.5.7

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Guia de Contribuição

- Mantenha o código limpo e bem documentado
- Adicione testes para novas funcionalidades
- Siga o padrão de commits convencionais
- Atualize a documentação conforme necessário

## 📝 License

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Autores

- **Flávio Mendes** - *Trabalho Inicial* - [GitHub](https://github.com/flmendes)

## 🙏 Agradecimentos

- [Spring Boot](https://spring.io/projects/spring-boot)
- [OpenAPI Generator](https://openapi-generator.tech/)
- [Paketo Buildpacks](https://paketo.io/)
- [RFC 7807](https://tools.ietf.org/html/rfc7807)

## 📞 Suporte

- 🐛 Issues: [GitHub Issues](https://github.com/flmendes/springboot-openapi-generator/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/flmendes/springboot-openapi-generator/discussions)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no GitHub!

**Desenvolvido com ❤️ usando Spring Boot e Java 21**
