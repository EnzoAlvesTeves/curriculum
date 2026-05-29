# ms-vagas

API para empresas, vagas e candidaturas com autenticação JWT via Keycloak.

## Endpoints principais

### Empresa
- `POST /api/empresas`
- `GET /api/empresas`
- `GET /api/empresas/{id}`
- `PUT /api/empresas/{id}`
- `DELETE /api/empresas/{id}`

### Vaga
- `POST /api/vagas` (somente usuario `tipo=RH`)
- `PUT /api/vagas/{id}` (somente usuario `tipo=RH`)
- `DELETE /api/vagas/{id}` (somente usuario `tipo=RH`)
- `GET /api/vagas/empresa/{idEmpresa}`
- `GET /api/vagas/minhas-criadas`
- `GET /api/vagas/minhas-candidaturas`

### Candidatura
- `POST /api/vagas/{idVaga}/candidaturas`
- `DELETE /api/vagas/{idVaga}/candidaturas`

## Como a autenticacao e validacao de usuario funciona

1. A API valida o JWT recebido (Resource Server).
2. Em operacoes de regra de negocio, o `ms-vagas` chama o `ms-usuario` via Feign em `GET /api/usuarios/me`.
3. O mesmo token recebido na request eh repassado no header `Authorization`.
4. Para criar/alterar/deletar vaga, o usuario retornado precisa ter `tipo=RH`.

## Variaveis de ambiente

- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_JWK_SET_URI`
- `MS_USUARIO_URL` (default `http://localhost:8091`)

## Exemplo rapido

```bash
curl -X POST http://localhost:8092/api/vagas \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Desenvolvedor Java",
    "descricao":"Atuar com Spring Boot",
    "salario": 8000,
    "beneficios":"VR, VT, Plano de saude",
    "idEmpresa": 1
  }'
```

