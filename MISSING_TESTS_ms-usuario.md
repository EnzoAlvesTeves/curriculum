# MISSING TESTS - ms-usuario

**Priority:** #3 (87% → 100%)  
**Status:** Pending test coverage improvements

## Summary

- **Methods not tested:** 8
- **Uncovered branches:** 6

---

## 1. Methods Not Tested (0% Coverage)

Methods that have no test coverage at all.
### KeycloakService.java - Line 62

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `refreshToken`  
**Signature:** `(Ljava/lang/String;)Lbr/com/senac/msusuario/dto/AuthTokenResponse;`  
**Instructions not covered:** 49

**Suggested Test:**
```java
@Test
void testrefreshToken_184506797 () {
    // TODO: Test implementation
    // Test refreshToken in KeycloakService.java
}
```

---
### KeycloakService.java - Line 198

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$obterTokenAdmin$7`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 11

**Suggested Test:**
```java
@Test
void testlambda$obterTokenAdmin$7_1328562547 () {
    // TODO: Test implementation
    // Test lambda$obterTokenAdmin$7 in KeycloakService.java
}
```

---
### KeycloakService.java - Line 174

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$alterarSenhaKeycloak$6`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 11

**Suggested Test:**
```java
@Test
void testlambda$alterarSenhaKeycloak$6_954567466 () {
    // TODO: Test implementation
    // Test lambda$alterarSenhaKeycloak$6 in KeycloakService.java
}
```

---
### KeycloakService.java - Line 146

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$deletarUsuarioKeycloak$5`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 11

**Suggested Test:**
```java
@Test
void testlambda$deletarUsuarioKeycloak$5_1954355169 () {
    // TODO: Test implementation
    // Test lambda$deletarUsuarioKeycloak$5 in KeycloakService.java
}
```

---
### KeycloakService.java - Line 115

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$criarUsuarioKeycloak$4`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 11

**Suggested Test:**
```java
@Test
void testlambda$criarUsuarioKeycloak$4_1702205181 () {
    // TODO: Test implementation
    // Test lambda$criarUsuarioKeycloak$4 in KeycloakService.java
}
```

---
### KeycloakService.java - Line 112

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$criarUsuarioKeycloak$3`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 6

**Suggested Test:**
```java
@Test
void testlambda$criarUsuarioKeycloak$3_379652937 () {
    // TODO: Test implementation
    // Test lambda$criarUsuarioKeycloak$3 in KeycloakService.java
}
```

---
### KeycloakService.java - Line 75

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$refreshToken$1`  
**Signature:** `(Lorg/springframework/http/HttpRequest;Lorg/springframework/http/client/ClientHttpResponse;)V`  
**Instructions not covered:** 11

**Suggested Test:**
```java
@Test
void testlambda$refreshToken$1_368329561 () {
    // TODO: Test implementation
    // Test lambda$refreshToken$1 in KeycloakService.java
}
```

---
### MsUsuarioApplication.java - Line 10

**Class:** `br.com.senac.msusuario.MsUsuarioApplication`  
**Method:** `main`  
**Signature:** `([Ljava/lang/String;)V`  
**Instructions not covered:** 5

**Suggested Test:**
```java
@Test
void testmain_532341885 () {
    // TODO: Test implementation
    // Test main in MsUsuarioApplication.java
}
```

---

## 2. Uncovered Branches

Methods with partially covered branches (if/else, switch statements, etc).
### KeycloakService.java - Line 87

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `criarUsuarioKeycloak`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testcriarUsuarioKeycloak_allBranches_629720421 () {
    // TODO: Test all branches in criarUsuarioKeycloak
    // Ensure all if/else paths are executed
}
```

---
### KeycloakService.java - Line 185

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `obterTokenAdmin`  
**Branches:** 2 missed / 2 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testobterTokenAdmin_allBranches_1992788051 () {
    // TODO: Test all branches in obterTokenAdmin
    // Ensure all if/else paths are executed
}
```

---
### KeycloakService.java - Line 229

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `toTokenResponse`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testtoTokenResponse_allBranches_165873749 () {
    // TODO: Test all branches in toTokenResponse
    // Ensure all if/else paths are executed
}
```

---
### KeycloakService.java - Line 243

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `str`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void teststr_allBranches_836911394 () {
    // TODO: Test all branches in str
    // Ensure all if/else paths are executed
}
```

---
### KeycloakService.java - Line 247

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `toLong`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testtoLong_allBranches_783059341 () {
    // TODO: Test all branches in toLong
    // Ensure all if/else paths are executed
}
```

---
### KeycloakService.java - Line 111

**Class:** `br.com.senac.msusuario.service.KeycloakService`  
**Method:** `lambda$criarUsuarioKeycloak$2`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testlambda$criarUsuarioKeycloak$2_allBranches_1327824820 () {
    // TODO: Test all branches in lambda$criarUsuarioKeycloak$2
    // Ensure all if/else paths are executed
}
```

---

## Implementation Notes

- Focus on testing the identified methods first
- For uncovered branches, ensure both condition outcomes are tested
- Consider using parameterized tests (@ParameterizedTest) for different branch paths
- Update/create unit tests in the appropriate test class

