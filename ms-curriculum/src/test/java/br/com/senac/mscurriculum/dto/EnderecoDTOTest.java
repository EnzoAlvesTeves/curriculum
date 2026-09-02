package br.com.senac.mscurriculum.dto;

class EnderecoDTOTest {

    @org.junit.jupiter.api.Test
    void testConstrutorVazio() {
        EnderecoDTO dto = new EnderecoDTO();

        org.junit.jupiter.api.Assertions.assertNull(dto.getId());
        org.junit.jupiter.api.Assertions.assertNull(dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertNull(dto.getRua());
        org.junit.jupiter.api.Assertions.assertNull(dto.getNumero());
        org.junit.jupiter.api.Assertions.assertNull(dto.getComplemento());
        org.junit.jupiter.api.Assertions.assertNull(dto.getCidade());
        org.junit.jupiter.api.Assertions.assertNull(dto.getEstado());
        org.junit.jupiter.api.Assertions.assertNull(dto.getCep());
        org.junit.jupiter.api.Assertions.assertNull(dto.getBairro());
        org.junit.jupiter.api.Assertions.assertNull(dto.getLatitude());
        org.junit.jupiter.api.Assertions.assertNull(dto.getLongitude());
    }

    @org.junit.jupiter.api.Test
    void testConstrutorCompleto() {
        java.math.BigDecimal latitude = new java.math.BigDecimal("-23.550520");
        java.math.BigDecimal longitude = new java.math.BigDecimal("-46.633308");

        EnderecoDTO dto = new EnderecoDTO(
                1L,
                10L,
                "Rua A",
                "100",
                "Apto 1",
                "São Paulo",
                "SP",
                "01000-000",
                "Centro",
                latitude,
                longitude
        );

        org.junit.jupiter.api.Assertions.assertAll(
                () -> org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Rua A", dto.getRua()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("100", dto.getNumero()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Apto 1", dto.getComplemento()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("São Paulo", dto.getCidade()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("SP", dto.getEstado()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("01000-000", dto.getCep()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Centro", dto.getBairro()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(latitude, dto.getLatitude()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(longitude, dto.getLongitude())
        );
    }

    @org.junit.jupiter.api.Test
    void testSetters() {
        EnderecoDTO dto = new EnderecoDTO();

        dto.setId(1L);
        dto.setIdCandidato(10L);
        dto.setRua("Rua B");
        dto.setNumero("200");
        dto.setComplemento("Casa");
        dto.setCidade("Campinas");
        dto.setEstado("SP");
        dto.setCep("13000-000");
        dto.setBairro("Centro");
        dto.setLatitude(new java.math.BigDecimal("-22.90556"));
        dto.setLongitude(new java.math.BigDecimal("-47.06083"));

        org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId());
        org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Rua B", dto.getRua());
        org.junit.jupiter.api.Assertions.assertEquals("200", dto.getNumero());
        org.junit.jupiter.api.Assertions.assertEquals("Casa", dto.getComplemento());
        org.junit.jupiter.api.Assertions.assertEquals("Campinas", dto.getCidade());
        org.junit.jupiter.api.Assertions.assertEquals("SP", dto.getEstado());
        org.junit.jupiter.api.Assertions.assertEquals("13000-000", dto.getCep());
        org.junit.jupiter.api.Assertions.assertEquals("Centro", dto.getBairro());
        org.junit.jupiter.api.Assertions.assertEquals(new java.math.BigDecimal("-22.90556"), dto.getLatitude());
        org.junit.jupiter.api.Assertions.assertEquals(new java.math.BigDecimal("-47.06083"), dto.getLongitude());
    }
}