# ms-usuario

Serviço de usuários e autenticação. Faz o cadastro de usuários (CANDIDATO e RH),
integra com o Keycloak para login/refresh de tokens JWT e expõe os dados do
usuário autenticado.

- **Porta:** 8091
- **Swagger:** http://localhost:8091/swagger-ui/index.html
- **Java:** 21

## Endpoints principais

### Autenticação (`/api/auth`)
- `POST /api/auth/login` — autentica no Keycloak e retorna o JWT
- `POST /api/auth/refresh` — renova o token a partir do refresh token

### Usuários (`/api/usuarios`)
- `POST /api/usuarios` — cria usuário (endpoint público)
- `GET /api/usuarios` — lista todos (autenticado)
- `GET /api/usuarios?ids=1,2,3` — busca por múltiplos IDs
- `GET /api/usuarios/me` — dados do usuário autenticado
- `GET /api/usuarios/{id}` — busca por ID
- `PUT /api/usuarios/{id}` — atualiza
- `DELETE /api/usuarios/{id}` — exclui (remove também do Keycloak)
- `PATCH /api/usuarios/me/senha` — altera a própria senha (valida a atual)
- `PATCH /api/usuarios/senha/username` — redefine senha por username

## Autenticação

O serviço é um Resource Server OAuth2: valida o JWT emitido pelo Keycloak.
O cadastro de usuário também cria o usuário correspondente no Keycloak.

## Variáveis de ambiente

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `KEYCLOAK_SERVER_URL`
- `KEYCLOAK_REALM`
- `KEYCLOAK_ADMIN_CLIENT_ID`, `KEYCLOAK_ADMIN_CLIENT_SECRET`
- `KEYCLOAK_USER_CLIENT_ID`
- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_JWK_SET_URI`

## Exemplo rápido

```bash
# Login
curl -X POST http://localhost:8091/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario@exemplo.com","senha":"Senha@123"}'
```

## Testes

Veja o guia na raiz do repositório (`README.md`, seção "Testes de integração").
