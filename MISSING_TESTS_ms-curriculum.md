# MISSING TESTS - ms-curriculum

**Priority:** #2 (97% → 100%)  
**Status:** Pending test coverage improvements

## Summary

- **Methods not tested:** 4
- **Uncovered branches:** 4

---

## 1. Methods Not Tested (0% Coverage)

Methods that have no test coverage at all.
### EnderecoService.java - Line 67

**Class:** `br.com.senac.mscurriculum.service.EnderecoService`  
**Method:** `deletar`  
**Signature:** `(Ljava/lang/Long;Lorg/springframework/security/oauth2/jwt/Jwt;)V`  
**Instructions not covered:** 30

**Suggested Test:**
```java
@Test
void testdeletar_820277665 () {
    // TODO: Test implementation
    // Test deletar in EnderecoService.java
}
```

---
### EnderecoService.java - Line 70

**Class:** `br.com.senac.mscurriculum.service.EnderecoService`  
**Method:** `lambda$deletar$1`  
**Signature:** `(Ljava/lang/Long;)Ljava/lang/RuntimeException;`  
**Instructions not covered:** 6

**Suggested Test:**
```java
@Test
void testlambda$deletar$1_1767608651 () {
    // TODO: Test implementation
    // Test lambda$deletar$1 in EnderecoService.java
}
```

---
### EnderecoService.java - Line 49

**Class:** `br.com.senac.mscurriculum.service.EnderecoService`  
**Method:** `lambda$alterar$0`  
**Signature:** `(Ljava/lang/Long;)Ljava/lang/RuntimeException;`  
**Instructions not covered:** 6

**Suggested Test:**
```java
@Test
void testlambda$alterar$0_1318093671 () {
    // TODO: Test implementation
    // Test lambda$alterar$0 in EnderecoService.java
}
```

---
### MsCurriculumApplication.java - Line 10

**Class:** `br.com.senac.mscurriculum.MsCurriculumApplication`  
**Method:** `main`  
**Signature:** `([Ljava/lang/String;)V`  
**Instructions not covered:** 5

**Suggested Test:**
```java
@Test
void testmain_691341783 () {
    // TODO: Test implementation
    // Test main in MsCurriculumApplication.java
}
```

---

## 2. Uncovered Branches

Methods with partially covered branches (if/else, switch statements, etc).
### CandidatoService.java - Line 101

**Class:** `br.com.senac.mscurriculum.service.CandidatoService`  
**Method:** `carregarCandidatoCompleto`  
**Branches:** 3 missed / 3 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testcarregarCandidatoCompleto_allBranches_1029871565 () {
    // TODO: Test all branches in carregarCandidatoCompleto
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 44

**Class:** `br.com.senac.mscurriculum.config.FeignConfig`  
**Method:** `parseLevel`  
**Branches:** 2 missed / 2 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testparseLevel_allBranches_1362842525 () {
    // TODO: Test all branches in parseLevel
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 56

**Class:** `br.com.senac.mscurriculum.config.FeignConfig`  
**Method:** `defaultString`  
**Branches:** 2 missed / 2 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testdefaultString_allBranches_908555902 () {
    // TODO: Test all branches in defaultString
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 60

**Class:** `br.com.senac.mscurriculum.config.FeignConfig`  
**Method:** `defaultInt`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testdefaultInt_allBranches_1379640516 () {
    // TODO: Test all branches in defaultInt
    // Ensure all if/else paths are executed
}
```

---

## Implementation Notes

- Focus on testing the identified methods first
- For uncovered branches, ensure both condition outcomes are tested
- Consider using parameterized tests (@ParameterizedTest) for different branch paths
- Update/create unit tests in the appropriate test class

