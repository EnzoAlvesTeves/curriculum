# Test Coverage Analysis - Index

## 📋 Relatórios de Cobertura de Testes Faltantes

Este diretório contém análises detalhadas dos testes unitários que faltam em cada módulo do projeto para atingir 100% de cobertura de testes.

---

## 📊 Resumo Executivo

| Módulo | Prioridade | Cobertura Atual | Meta | Métodos não Testados | Branches não Cobertos | Arquivo |
|--------|-----------|-----------------|------|----------------------|----------------------|---------|
| **ms-vagas** | #1 | 98% | 100% | 1 | 6 | [MISSING_TESTS_ms-vagas.md](./MISSING_TESTS_ms-vagas.md) |
| **ms-curriculum** | #2 | 97% | 100% | 4 | 4 | [MISSING_TESTS_ms-curriculum.md](./MISSING_TESTS_ms-curriculum.md) |
| **ms-usuario** | #3 | 87% | 100% | 8 | 6 | [MISSING_TESTS_ms-usuario.md](./MISSING_TESTS_ms-usuario.md) |
| **bff-curriculum** | #4 | 41% | 100% | 53 | 9 | [MISSING_TESTS_bff-curriculum.md](./MISSING_TESTS_bff-curriculum.md) |

**Totais:** 66 métodos não testados | 25 branches não cobertos | **91 issues de cobertura**

---

## 🎯 Prioridades de Implementação

### 1️⃣ **ms-vagas** - 98% → 100% (Mais próximo de 100%)
- **Impacto:** Mínimo - apenas 7 issues
- **Esforço:** Baixo
- **Tempo estimado:** 1-2 horas
- **Issues principais:**
  - `MsVagasApplication.main()` - não testado
  - Branches faltando em `validarRh()`, `validarCandidato()` e config
  - Testes em `FeignConfig` (parseLevel, defaultString, defaultInt)

### 2️⃣ **ms-curriculum** - 97% → 100%
- **Impacto:** Baixo - 8 issues
- **Esforço:** Baixo-Médio
- **Tempo estimado:** 2-3 horas
- **Issues principais:**
  - `EnderecoService.deletar()` completamente não testado
  - Lambdas em `EnderecoService` sem cobertura
  - `MsCurriculumApplication.main()` não testado
  - Branches em `CandidatoService.carregarCandidatoCompleto()`

### 3️⃣ **ms-usuario** - 87% → 100%
- **Impacto:** Médio - 14 issues
- **Esforço:** Médio
- **Tempo estimado:** 3-4 horas
- **Issues principais:**
  - `KeycloakService` com múltiplas operações não testadas
  - `refreshToken()` completamente não testado
  - Lambdas de interceptadores HTTP sem cobertura
  - Métodos auxiliares `str()`, `toLong()` sem branches testados
  - `MsUsuarioApplication.main()` não testado

### 4️⃣ **bff-curriculum** - 41% → 100% (Maior desafio)
- **Impacto:** Alto - 62 issues
- **Esforço:** Alto
- **Tempo estimado:** 6-8 horas
- **Issues principais:**
  - Todos os clients Feign não testados (UsuarioClient, CandidatoClient, VagaClient, etc)
  - `RestTemplateConfig` e `RestTemplateLoggingInterceptor` sem cobertura
  - `BffCurriculumApplication.main()` não testado
  - Enum `Sexo` com branches não cobertos

---

## 📁 Estrutura dos Arquivos de Relatório

Cada arquivo `MISSING_TESTS_<modulo>.md` contém:

### Seção 1: Métodos não Testados (0% Coverage)
Apresenta cada método completamente não testado com:
- Classe e localização (arquivo + linha)
- Assinatura do método
- Número de instruções não cobertas
- Snippet sugerido de teste

**Exemplo:**
```
### KeycloakService.java - Line 62

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `refreshToken`  
**Signature:** `(Ljava/lang/String;)Lbr/com/senac/msusuario/dto/AuthTokenResponse;`  
**Instructions not covered:** 49

**Suggested Test:**
```java
@Test
void testrefreshToken_xyz () {
    // TODO: Test implementation
    // Test refreshToken in KeycloakService.java
}
```
```

### Seção 2: Branches não Cobertos
Apresenta métodos com branches parcialmente cobertos:
- Classe e localização
- Método e número da linha
- Quantidade de branches faltando / total de branches
- Orientação sobre o que testar
- Snippet sugerido de teste

**Exemplo:**
```
### EmpresaService.java - Line 75

**Class:** `br.com.senac.msvagas.service.EmpresaService`  
**Method:** `validarRh`  
**Branches:** 1 missed / 3 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testvalidarRh_allBranches_xyz () {
    // TODO: Test all branches in validarRh
    // Ensure all if/else paths are executed
}
```
```

---

## 🚀 Como Usar Estes Relatórios

### Passo 1: Escolha um módulo por prioridade
Comece pelo ms-vagas (prioridade #1) pois requer menos esforço e tem impacto imediato.

### Passo 2: Leia o relatório completo
Abra o arquivo `MISSING_TESTS_<modulo>.md` e entenda o que precisa ser testado.

### Passo 3: Implemente os testes
Use os snippets sugeridos como ponto de partida e implemente os testes faltantes.

### Passo 4: Verifique a cobertura
```bash
cd <modulo>
mvn clean test jacoco:report
```

### Passo 5: Revise o relatório HTML
Abra `<modulo>/target/site/jacoco/index.html` para visualizar a cobertura em tempo real.

---

## 💡 Dicas para Implementação

### Para Métodos Não Testados (0% Coverage)
1. **Identifique as dependências** - quais serviços/repositórios são usados?
2. **Use mocks** - crie mocks das dependências com `@Mock` e `Mockito`
3. **Teste o caminho feliz** - primeiro teste o caso de sucesso
4. **Teste exceções** - depois teste casos de erro
5. **Valide resultados** - use `assertEquals`, `assertTrue`, `verify`

### Para Branches Não Cobertos
1. **Identifique as condições** - quais são os if/else, switch, ternário?
2. **Crie testes parametrizados** - use `@ParameterizedTest` para vários cenários
3. **Teste cada caminho** - garanta que tanto a verdade quanto falsa são executadas
4. **Valide comportamento diferente** - cada branch deve ter comportamento diferente

### Exemplo de teste parametrizado para branches:
```java
@ParameterizedTest
@ValueSource(strings = {"RH", "CANDIDATO", "OUTRO"})
void testValidarRh_withDifferentRoles(String role) {
    // Arrange
    UsuarioMeResponse usuario = new UsuarioMeResponse(1L, TipoUsuario.valueOf(role), "name", "email", "id");
    
    // Act & Assert
    if ("RH".equals(role)) {
        // Should pass
        assertDoesNotThrow(() -> service.validarRh(usuario));
    } else {
        // Should throw exception
        assertThrows(ResponseStatusException.class, () -> service.validarRh(usuario));
    }
}
```

---

## 📈 Progresso Esperado

| Etapa | Módulo | Meta | Tempo Estimado |
|-------|--------|------|---|
| 1 | ms-vagas | 98% → 100% | 1-2h |
| 2 | ms-curriculum | 97% → 100% | 2-3h |
| 3 | ms-usuario | 87% → 100% | 3-4h |
| 4 | bff-curriculum | 41% → 100% | 6-8h |
| **Total** | **Todos** | **100%** | **12-17h** |

---

## 📝 Notas Importantes

1. **main() methods** - Os métodos main() podem ser ignorados em tests, pois são apenas entry points da aplicação. Considere usar `@Tested` ou adicionar testes simples se necessário.

2. **Lambda expressions** - Lambdas geradas pelo compilador (como `lambda$x$y`) devem ser testadas através dos métodos que as usam.

3. **FeignConfig e RestTemplateConfig** - Estes são principalmente configurações que precisam ser testadas em testes de integração ou contexto de Spring.

4. **Clients (Feign)** - Os clients Feign podem ser difíceis de testar sem mocks do servidor. Considere usar `@MockRestServiceServer` ou similar.

---

## ✅ Verificação Final

Após implementar todos os testes:

```bash
# Gerar relatório de cobertura para todos os módulos
mvn -pl ms-vagas,ms-curriculum,ms-usuario,bff-curriculum clean test jacoco:report

# Verificar se todos atingiram 100%
echo "Verifique os arquivos:"
echo "- ms-vagas/target/site/jacoco/index.html"
echo "- ms-curriculum/target/site/jacoco/index.html"
echo "- ms-usuario/target/site/jacoco/index.html"
echo "- bff-curriculum/target/site/jacoco/index.html"
```

---

**Última atualização:** 2026-10-13  
**Status:** 📊 Análise Completa - Aguardando Implementação dos Testes
