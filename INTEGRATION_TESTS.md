# Testes de Integração — Guia de Execução

## Pré-requisitos

- Java 21
- Maven (ou use o `mvnw` de cada módulo)
- **Docker Desktop em execução** — os Testcontainers sobem MySQL 8.0 e Keycloak 26.0 automaticamente

## Como executar

### Apenas os testes de integração (recomendado)

```bash
# ms-usuario
cd ms-usuario
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

# ms-curriculum
cd ms-curriculum
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

# ms-vagas
cd ms-vagas
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false
```

### Todos os testes (unitários + integração)

```bash
cd ms-usuario && mvn test
cd ms-curriculum && mvn test
cd ms-vagas && mvn test
```

## Estrutura dos testes

```
ms-usuario/src/test/java/br/com/senac/msusuario/integration/
  ├── AbstractIntegrationTest.java      ← base: MySQL + Keycloak via Testcontainers
  ├── KeycloakTestHelper.java           ← utilitário para obter tokens JWT
  └── UsuarioIntegrationTest.java       ← CRUD de usuários + autenticação

ms-curriculum/src/test/java/br/com/senac/mscurriculum/integration/
  ├── AbstractIntegrationTest.java      ← base: MySQL + Keycloak via Testcontainers
  ├── TokenHelper.java                  ← utilitário para criar usuários no Keycloak e obter tokens
  └── CandidatoIntegrationTest.java     ← CRUD de candidatos + endereço + educação + experiência + habilidade

ms-vagas/src/test/java/br/com/senac/msvagas/integration/
  ├── AbstractIntegrationTest.java      ← base: MySQL + Keycloak via Testcontainers
  ├── TokenHelper.java                  ← utilitário para criar usuários no Keycloak e obter tokens
  └── VagaIntegrationTest.java          ← CRUD de vagas + empresas + candidaturas
```

## Arquitetura dos testes

```
Teste (@SpringBootTest)
    ↓
AbstractIntegrationTest
    ├── MySQLContainer (mysql:8.0)     ← banco real com migrations Flyway
    └── KeycloakContainer (keycloak:26.0) ← OAuth2 real com realm curriculum importado

Spring Security (JWT)
    └── valida tokens emitidos pelo KeycloakContainer

MsUsuarioClient (Feign)  ← ms-curriculum e ms-vagas
    └── @MockBean          ← mockado (o ms-usuario real não é necessário)
```

## O que é testado

### ms-usuario
- `POST /api/usuarios` — criar candidato e RH (endpoint público)
- `POST /api/auth/login` — autenticação via Keycloak, retorno de JWT
- `POST /api/auth/refresh` — renovação de token
- `GET /api/usuarios/me` — dados do usuário autenticado
- `GET /api/usuarios` — listar todos (autenticado)
- `GET /api/usuarios/{id}` — busca por ID
- `GET /api/usuarios?ids=...` — busca por múltiplos IDs
- `PUT /api/usuarios/{id}` — atualização
- `PATCH /api/usuarios/me/senha` — alteração de senha (valida senha atual)
- `DELETE /api/usuarios/{id}` — exclusão (remove do Keycloak também)
- Validações: 409 email duplicado, 400 payload inválido, 401 sem token, 404 ID inexistente

### ms-curriculum
- `POST /api/candidatos` — cadastro de candidato (apenas CANDIDATO, 403 para RH)
- `GET /api/candidatos/{id}` — busca por ID
- `GET /api/candidatos/me` — perfil do usuário autenticado
- `PUT /api/candidatos` — atualização de dados
- `DELETE /api/candidatos` — exclusão do perfil
- Endereço: cadastrar, buscar, atualizar
- Educações: cadastrar, listar, excluir
- Experiências: cadastrar, listar, excluir
- Habilidades: cadastrar, listar, excluir
- GET completo com todas as relações aninhadas
- Validações: 409 duplicado, 403 tipo errado, 401 sem token, 404 inexistente

### ms-vagas
- Empresas: criar (RH), listar, buscar por ID, atualizar, excluir
- Vagas: criar (RH), listar por empresa, listar criadas pelo usuário, atualizar, excluir
- Candidaturas: candidatar-se (CANDIDATO), listar candidaturas, remover candidatura
- `GET /api/vagas/{id}/candidatos` — RH lista candidatos de uma vaga
- Validações: 403 tipo errado, 409 candidatura duplicada, 401 sem token

## Detalhes técnicos

### Por que o MsUsuarioClient é @MockBean no ms-curriculum e ms-vagas?
O ms-curriculum e ms-vagas chamam o ms-usuario via Feign apenas para obter os dados do
usuário autenticado (`GET /api/usuarios/me`). Em testes de integração de cada serviço
isoladamente, mockamos esse cliente para não precisar subir o ms-usuario como dependência.
O JWT real do Keycloak Container já garante que a segurança OAuth2 está funcionando.

### Por que o realm-export.json está no classpath de teste?
O `KeycloakContainer.withRealmImportFile()` precisa do arquivo no classpath de teste.
É a cópia do mesmo realm de desenvolvimento, incluindo os clients `curriculum-app-admin`
e `curriculum-app-user` com as mesmas configurações.

### Tempo de execução estimado
- Primeira execução: ~3-5 min (pull das imagens Docker + inicialização dos containers)
- Execuções subsequentes com `.withReuse(true)`: ~30-60s por módulo
  (containers são reutilizados entre runs na mesma sessão Docker)
