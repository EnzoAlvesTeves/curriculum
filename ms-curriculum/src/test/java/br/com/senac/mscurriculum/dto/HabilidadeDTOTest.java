package br.com.senac.mscurriculum.dto;

class HabilidadeDTOTest {

    @org.junit.jupiter.api.Test
    void testConstrutorVazio() {
        HabilidadeDTO dto = new HabilidadeDTO();

        org.junit.jupiter.api.Assertions.assertNull(dto.getId());
        org.junit.jupiter.api.Assertions.assertNull(dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertNull(dto.getDescricao());
        org.junit.jupiter.api.Assertions.assertNull(dto.getNivel());
    }

    @org.junit.jupiter.api.Test
    void testConstrutorCompleto() {
        HabilidadeDTO dto = new HabilidadeDTO(1L, 10L, "Java", "Avançado");

        org.junit.jupiter.api.Assertions.assertAll(
                () -> org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Java", dto.getDescricao()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Avançado", dto.getNivel())
        );
    }

    @org.junit.jupiter.api.Test
    void testSetters() {
        HabilidadeDTO dto = new HabilidadeDTO();

        dto.setId(1L);
        dto.setIdCandidato(10L);
        dto.setDescricao("Spring Boot");
        dto.setNivel("Intermediário");

        org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId());
        org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Spring Boot", dto.getDescricao());
        org.junit.jupiter.api.Assertions.assertEquals("Intermediário", dto.getNivel());
    }
}