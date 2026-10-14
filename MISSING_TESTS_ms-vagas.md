# MISSING TESTS - ms-vagas

**Priority:** #1 (98% → 100%)  
**Status:** Pending test coverage improvements

## Summary

- **Methods not tested:** 1
- **Uncovered branches:** 6

---

## 1. Methods Not Tested (0% Coverage)

Methods that have no test coverage at all.
### MsVagasApplication.java - Line 10

**Class:** `br.com.senac.msvagas.MsVagasApplication`  
**Method:** `main`  
**Signature:** `([Ljava/lang/String;)V`  
**Instructions not covered:** 5

**Suggested Test:**
```java
@Test
void testmain_1838479585 () {
    // TODO: Test implementation
    // Test main in MsVagasApplication.java
}
```

---

## 2. Uncovered Branches

Methods with partially covered branches (if/else, switch statements, etc).
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
void testvalidarRh_allBranches_647808064 () {
    // TODO: Test all branches in validarRh
    // Ensure all if/else paths are executed
}
```

---
### VagaService.java - Line 189

**Class:** `br.com.senac.msvagas.service.VagaService`  
**Method:** `validarRh`  
**Branches:** 1 missed / 3 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testvalidarRh_allBranches_1217942154 () {
    // TODO: Test all branches in validarRh
    // Ensure all if/else paths are executed
}
```

---
### VagaService.java - Line 195

**Class:** `br.com.senac.msvagas.service.VagaService`  
**Method:** `validarCandidato`  
**Branches:** 1 missed / 3 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testvalidarCandidato_allBranches_2031632728 () {
    // TODO: Test all branches in validarCandidato
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 44

**Class:** `br.com.senac.msvagas.config.FeignConfig`  
**Method:** `parseLevel`  
**Branches:** 2 missed / 2 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testparseLevel_allBranches_1722173325 () {
    // TODO: Test all branches in parseLevel
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 56

**Class:** `br.com.senac.msvagas.config.FeignConfig`  
**Method:** `defaultString`  
**Branches:** 2 missed / 2 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testdefaultString_allBranches_208318481 () {
    // TODO: Test all branches in defaultString
    // Ensure all if/else paths are executed
}
```

---
### FeignConfig.java - Line 60

**Class:** `br.com.senac.msvagas.config.FeignConfig`  
**Method:** `defaultInt`  
**Branches:** 1 missed / 1 covered

**What needs testing:**
- Missing branch coverage in conditional statements (if/else, switch)
- Need to test both true and false paths in the method

**Suggested Test:**
```java
@Test
void testdefaultInt_allBranches_1722381938 () {
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

