# ms-curriculum

Serviço de currículo do candidato: dados pessoais, endereço, educações,
experiências e habilidades. Apenas usuários do tipo CANDIDATO podem cadastrar
o próprio perfil.

- **Porta:** 8090
- **Swagger:** http://localhost:8090/swagger-ui/index.html
- **Java:** 21

## Endpoints principais

### Candidato (`/api/candidatos`)
- `POST /api/candidatos` — cadastra o perfil (somente CANDIDATO)
- `PUT /api/candidatos` — atualiza os dados do candidato autenticado
- `GET /api/candidatos/{id}` — busca por ID (com relações aninhadas)
- `GET /api/candidatos/me` — perfil do usuário autenticado
- `DELETE /api/candidatos` — exclui o próprio perfil

### Endereço (`/api/candidatos/{candidatoId}/endereco`)
- `POST` · `GET` · `PUT` · `DELETE`

### Educações (`/api/candidatos/{candidatoId}/educacoes`)
- `POST` · `GET` · `PUT /{educacaoId}` · `DELETE /{educacaoId}`

### Experiências (`/api/candidatos/{candidatoId}/experiencias`)
- `POST` · `GET` · `PUT /{experienciaId}` · `DELETE /{experienciaId}`

### Habilidades (`/api/candidatos/{candidatoId}/habilidades`)
- `POST` · `GET` · `PUT /{habilidadeId}` · `DELETE /{habilidadeId}`

## Autenticação

Resource Server OAuth2: valida o JWT do Keycloak. Para resolver o usuário
autenticado e seu tipo (CANDIDATO/RH), chama o `ms-usuario` via Feign em
`GET /api/usuarios/me`, repassando o mesmo token.

## Variáveis de ambiente

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `MS_USUARIO_URL` (default `http://localhost:8091`)
- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_JWK_SET_URI`

## Exemplo rápido

```bash
curl -X POST http://localhost:8090/api/candidatos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome":"Carlos Curriculo",
    "email":"candidato@exemplo.com",
    "sexo":"MASCULINO",
    "telefone":"(11) 91234-5678",
    "dataNascimento":"1995-06-15",
    "resumoProfissional":"Desenvolvedor Java Pleno"
  }'
```

## Testes

Veja o guia na raiz do repositório (`README.md`, seção "Testes de integração").
