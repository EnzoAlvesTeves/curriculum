# bff-curriculum

Backend for Frontend (BFF). Serve as páginas web (Thymeleaf) e centraliza as
chamadas aos microserviços (`ms-usuario`, `ms-curriculum`, `ms-vagas`), sendo o
ponto único de acesso para o navegador.

- **Porta interna:** 8080
- **Porta no host (via docker-compose):** 8095 — acesse http://localhost:8095
- **Java:** 17

> A porta interna é 8080; no `docker-compose.yaml` ela é publicada em 8095 porque
> a 8080 do host é usada pelo Jenkins.

## Rotas (telas Thymeleaf)

### Login (`/`)
- `GET /` — tela de login

### Usuário (`/usuario`)
- `GET /usuario/cadastrar` · `POST /usuario/cadastrar`
- `GET /usuario/esqueci-senha` · `POST /usuario/esqueci-senha`
- `GET /usuario/home/{id}`
- `POST /usuario/login`

### Candidato (`/candidato`)
- `GET /candidato/{id}` · `GET /candidato/lista`
- `GET /candidato/cadastrar` · `POST /candidato/cadastrar`
- `POST /candidato/editar`

### Vaga (`/vaga`)
- `POST /vaga/cadastrar`
- `GET /vaga/cadastrar/usuario/{usuarioId}`
- `GET /vaga/lista/usuario/{usuarioId}`

### Candidato-Vaga (`/candidato-vaga`)
- `POST /candidato-vaga/candidatura`
- `GET /candidato-vaga/vaga/{vagaId}/usuario/{usuarioId}` — candidatos por vaga
- `GET /candidato-vaga/candidato/{candidatoId}` — vagas do candidato
- `DELETE /candidato-vaga/{id}` — remover candidatura

## Variáveis de ambiente

- `MS_USUARIO` (default `http://localhost:8091`)
- `MS_CURRICULUM` (default `http://localhost:8090`)
- `MS_VAGAS` (default `http://localhost:8092`)

## Testes

Veja o guia na raiz do repositório (`README.md`, seção "Testes").
