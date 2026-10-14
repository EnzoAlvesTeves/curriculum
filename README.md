# Curriculum — Plataforma de Currículos e Vagas

Sistema baseado em microserviços para gestão de currículos, vagas e candidaturas,
com um BFF servindo o frontend. Autenticação via Keycloak (OAuth2/JWT), banco MySQL,
API Gateway com Kong e esteira de CI/CD local com Jenkins + SonarQube.

Este README é o guia completo para rodar o projeto **do zero**. Cada serviço tem
também seu próprio README com detalhes de API:
[ms-usuario](ms-usuario/README.md) ·
[ms-curriculum](ms-curriculum/README.md) ·
[ms-vagas](ms-vagas/README.md) ·
[bff-curriculum](bff-curriculum/README.md)

---

## Sumário

- [Arquitetura](#arquitetura)
- [Serviços e portas](#serviços-e-portas)
- [Pré-requisitos](#pré-requisitos)
- [Rodando o projeto](#rodando-o-projeto)
- [Testes](#testes)
- [Esteira CI/CD (Jenkins + SonarQube)](#esteira-cicd-jenkins--sonarqube)
- [Troubleshooting](#troubleshooting)

---

## Arquitetura

```
Browser → BFF → Kong (gateway) → Microserviços → MySQL
                                      ↓
                                  Keycloak (OAuth2/JWT)
```

Cada microserviço tem sua própria responsabilidade, banco (schema) e porta.
O **BFF** centraliza as chamadas e é o ponto de acesso do frontend. Todos os
serviços se comunicam internamente pela rede Docker `curriculum-net`.

- **ms-usuario** — usuários e autenticação (integra com o Keycloak)
- **ms-curriculum** — currículo do candidato (endereço, educação, experiência, habilidade)
- **ms-vagas** — empresas, vagas e candidaturas
- **bff-curriculum** — telas (Thymeleaf) e orquestração das chamadas

---

## Serviços e portas

| Serviço         | Porta (host) | URL / Swagger                                      |
|-----------------|--------------|----------------------------------------------------|
| ms-curriculum   | 8090         | http://localhost:8090/swagger-ui/index.html        |
| ms-usuario      | 8091         | http://localhost:8091/swagger-ui/index.html        |
| ms-vagas        | 8092         | http://localhost:8092/swagger-ui/index.html        |
| bff-curriculum  | 8095         | http://localhost:8095                              |
| Kong (proxy)    | 8000 / 8001  | gateway / admin                                    |
| Keycloak        | 8093         | http://localhost:8093                              |
| MySQL           | 3306         | —                                                  |
| SonarQube       | 9000         | http://localhost:9000                              |
| Jenkins         | 8080 / 50000 | http://localhost:8080                              |

> O bff roda na porta 8080 **internamente**, publicada em 8095 no host (a 8080 do
> host é do Jenkins).

---

## Pré-requisitos

| Ferramenta        | Observação                                                      |
|-------------------|-----------------------------------------------------------------|
| Docker + Compose  | Docker Desktop em execução (`docker --version`, `docker compose version`) |
| Git               | `git --version`                                                 |
| Java 21           | Necessário para rodar/testar os `ms-*` localmente (o bff usa Java 17) |

> Para apenas **subir o sistema via Docker** você não precisa de Java/Maven na
> máquina — tudo é compilado dentro dos containers. O Java 21 só é necessário se
> for rodar os testes/análise localmente (fora do Docker).

---

## Rodando o projeto

### Opção 1 — Script automático

```bash
# Linux / macOS / Git Bash
chmod +x build_and_run.sh
./build_and_run.sh
```

```powershell
# Windows (PowerShell)
powershell -ExecutionPolicy Bypass -File build_and_run.ps1
```

### Opção 2 — Docker Compose manual

```bash
# Sobe o sistema de aplicação (bancos, keycloak, microserviços, bff, gateway)
docker compose up -d --build \
  curriculum-db keycloak \
  ms-usuario ms-curriculum ms-vagas bff-curriculum kong
```

Acesse o frontend em **http://localhost:8095**.

### Parar o projeto

```bash
docker compose down                               # para os containers
docker compose down --remove-orphans --volumes    # para e remove volumes (reset total)
```

---

## Testes

Pré-requisitos: **Java 21** e **Docker Desktop em execução** (os testes de
integração usam Testcontainers, que sobem MySQL 8.0 e Keycloak 26.0 automaticamente).

No macOS, aponte o `JAVA_HOME` para o JDK 21 antes de rodar:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Testes unitários + integração (por módulo)

```bash
cd ms-usuario    && ./mvnw test
cd ms-curriculum && ./mvnw test
cd ms-vagas      && ./mvnw test
cd bff-curriculum && ./mvnw test
```

### Apenas os testes de integração

```bash
cd ms-usuario    && ./mvnw test -Dtest="*IntegrationTest" -DfailIfNoTests=false
cd ms-curriculum && ./mvnw test -Dtest="*IntegrationTest" -DfailIfNoTests=false
cd ms-vagas      && ./mvnw test -Dtest="*IntegrationTest" -DfailIfNoTests=false
```

### Como os testes de integração funcionam

```
Teste (@SpringBootTest)
  → AbstractIntegrationTest
      ├── MySQLContainer (mysql:8.0)         ← banco real com migrations Flyway
      └── KeycloakContainer (keycloak:26.0)  ← OAuth2 real, realm "curriculum" importado
```

- O realm é importado de `src/test/resources/keycloak/realm-export.json` (cópia do
  realm de desenvolvimento, com os clients `curriculum-app-admin` e `curriculum-app-user`).
- Em `ms-curriculum` e `ms-vagas`, o cliente Feign do `ms-usuario` é substituído por
  `@MockBean` — não é preciso subir o `ms-usuario` para testar esses serviços isoladamente.

Tempo estimado: ~3-5 min na primeira execução (pull das imagens Docker), ~30-60s nas
seguintes (containers reutilizados na mesma sessão Docker).

---

## Esteira CI/CD (Jenkins + SonarQube)

Esteira local que, a cada commit numa branch `release/*`:

1. É disparada pelo Jenkins (polling do GitHub a cada 1 minuto)
2. Builda e testa os 4 módulos, com cobertura via JaCoCo
3. Envia a análise ao SonarQube
4. **Bloqueia** se o Quality Gate reprovar
5. Se aprovado, reconstrói e sobe os containers (deploy)

### Componentes

| Serviço      | Imagem                  | Porta (host) | Papel                       |
|--------------|-------------------------|--------------|-----------------------------|
| sonarqube    | `sonarqube:community`   | 9000         | Servidor de análise         |
| sonarqube-db | `postgres:16`           | —            | Banco do SonarQube          |
| jenkins      | `./jenkins` (lts-jdk21) | 8080 / 50000 | Orquestra o CI/CD           |

### 1. Subir SonarQube e Jenkins

```bash
docker compose up -d --build sonarqube-db sonarqube jenkins
```

A primeira subida do Jenkins constrói a imagem (Docker CLI + docker compose + plugins).
Acompanhe o SonarQube até logar `SonarQube is operational`:

```bash
docker compose logs -f sonarqube
```

### 2. Configurar o SonarQube (`http://localhost:9000`)

1. Login `admin` / `admin` → troque a senha.
2. Gere um token: avatar → **My Account → Security → Generate Tokens** → tipo
   **Global Analysis Token** → copie o valor (exibido uma única vez).
3. Crie o **webhook** que avisa o Jenkins o resultado do Quality Gate.
   O jeito mais direto é acessar a URL (logado como admin):
   `http://localhost:9000/admin/webhooks`
   (pelo menu: **Administration → Configuration → Webhooks**). Clique em **Create**:
   - **Name:** `jenkins`
   - **URL:** `http://jenkins:8080/sonarqube-webhook/` (a barra final é obrigatória)
   - **Secret:** deixe vazio

   > **Erro "Invalid URL: loopback, ... site-local ... not allowed"?** O SonarQube 26+
   > bloqueia webhooks para IPs privados (proteção SSRF), e o Jenkins fica num IP interno
   > da rede Docker. O `docker-compose.yaml` já define `SONAR_VALIDATEWEBHOOKS=false` no
   > serviço `sonarqube` para liberar isso em ambiente local. Se o container subiu antes
   > dessa configuração, recrie-o: `docker compose up -d sonarqube`.

### 3. Configurar o Jenkins (`http://localhost:8080`)

Senha inicial:

```bash
docker compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Cole a senha → **Install suggested plugins** → crie o admin. Depois:

**3.1 Credencial do token do Sonar**
- **Manage Jenkins → Credentials → System → Global credentials → Add Credentials**
- **Kind:** Secret text · **Secret:** token do passo 2 · **ID:** `sonar-token`

**3.2 Servidor SonarQube**
- **Manage Jenkins → System → SonarQube servers → Add SonarQube**
- **Name:** `SonarQube` (exatamente esse — é o nome usado no `Jenkinsfile`)
- **Server URL:** `http://sonarqube:9000`
- **Server authentication token:** a credencial `sonar-token`
- Marque **Enable injection of SonarQube server configuration**

**3.3 Pipeline Multibranch (dispara só em `release/*`)**
- **New Item** → nome `curriculum-cicd` → **Multibranch Pipeline** → OK
- **Branch Sources → Add source → Git**
  - **Project Repository:** `https://github.com/EnzoAlvesTeves/curriculum.git`
  - **Credentials:** adicione as do GitHub se o repo for privado
- **Behaviours → Filter by name (with regular expression):** `release/.*`
- **Build Configuration:** *by Jenkinsfile*, Script Path `Jenkinsfile`
- **Scan Multibranch Pipeline Triggers → Periodically if not otherwise run → 1 minute**
- **Save**

> **Onde fica o polling de 1 minuto** (os dois já estão configurados):
> 1. No `Jenkinsfile`: `triggers { pollSCM('* * * * *') }` detecta **novos commits**
>    numa branch já descoberta.
> 2. Na UI (passo 3.3): *Scan Triggers → Periodically → 1 minute* detecta **branches
>    novas** `release/*`.

### 4. Usar a esteira

```bash
git checkout -b release/1.0.0
git commit -am "release 1.0.0"
git push -u origin release/1.0.0
```

Em ~1 min o Jenkins detecta a branch e roda a pipeline. Acompanhe em **Stage View**.
Por módulo: `clean verify` → `sonar:sonar` → **Quality Gate** (se reprovar, aborta e
nada é deployado). Passando os 4, o estágio **Deploy** reconstrói e sobe os containers.
Branches que não sejam `release/*` são ignoradas.

### Análise manual sem Jenkins (teste rápido)

Requer o SonarQube no ar (passo 1) e um token (passo 2):

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # bff-curriculum usa Java 17

./mvnw clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=SEU_TOKEN
```

---

## Troubleshooting

**`Could not find a valid Docker environment` / HTTP 400 ao rodar testes**
O Testcontainers 1.21.3 (padrão do Spring Boot 3.5.5) é incompatível com o Docker
Engine 29+. Os `pom.xml` já fixam `testcontainers.version` 1.21.4, que corrige isso.
Garanta que o Docker Desktop está rodando.

**Testes falham com erro de versão / não compilam**
Os `ms-*` exigem **Java 21**. No macOS: `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`.
No IntelliJ, confira o SDK do projeto e o runner de testes.

**`Resource with path keycloak/realm-export.json could not be found`**
O arquivo de realm precisa estar em `src/test/resources/keycloak/realm-export.json`
de cada módulo (cópia de `keycloak/realm-export.json` na raiz). Já está versionado.

**`Connection refused ... localhost:9999` nos testes de integração**
O cliente Feign do `ms-usuario` deve ser `@MockBean` (não `@Mock`) nos testes de
integração, para o Spring injetar o mock no contexto.

**Quality Gate fica "pendurado" no Jenkins**
O `waitForQualityGate` depende do webhook do SonarQube (passo 2.3). Sem ele, o estágio
espera até o timeout (10 min).

**Cobertura aparece 0% no SonarQube**
Rode a fase `verify`/`test` antes do `sonar:sonar`, para o `target/site/jacoco/jacoco.xml`
existir quando a análise for enviada.
