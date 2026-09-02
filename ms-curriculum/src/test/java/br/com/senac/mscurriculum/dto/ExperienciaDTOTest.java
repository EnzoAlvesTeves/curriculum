package br.com.senac.mscurriculum.dto;

class ExperienciaDTOTest {

    @org.junit.jupiter.api.Test
    void testConstrutorVazio() {
        ExperienciaDTO dto = new ExperienciaDTO();

        org.junit.jupiter.api.Assertions.assertNull(dto.getId());
        org.junit.jupiter.api.Assertions.assertNull(dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertNull(dto.getCargo());
        org.junit.jupiter.api.Assertions.assertNull(dto.getEmpresa());
        org.junit.jupiter.api.Assertions.assertNull(dto.getResumo());
        org.junit.jupiter.api.Assertions.assertNull(dto.getDataInicio());
        org.junit.jupiter.api.Assertions.assertNull(dto.getDataFim());
    }

    @org.junit.jupiter.api.Test
    void testConstrutorCompleto() {
        java.time.LocalDate dataInicio = java.time.LocalDate.of(2020, 1, 1);
        java.time.LocalDate dataFim = java.time.LocalDate.of(2021, 12, 31);

        ExperienciaDTO dto = new ExperienciaDTO(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs REST",
                dataInicio,
                dataFim
        );

        org.junit.jupiter.api.Assertions.assertAll(
                () -> org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Desenvolvedor Pleno", dto.getCargo()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Empresa XYZ", dto.getEmpresa()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Desenvolvimento de APIs REST", dto.getResumo()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(dataInicio, dto.getDataInicio()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(dataFim, dto.getDataFim())
        );
    }

    @org.junit.jupiter.api.Test
    void testSetters() {
        ExperienciaDTO dto = new ExperienciaDTO();

        dto.setId(1L);
        dto.setIdCandidato(10L);
        dto.setCargo("Desenvolvedor Júnior");
        dto.setEmpresa("Empresa ABC");
        dto.setResumo("Atuação em APIs");
        dto.setDataInicio(java.time.LocalDate.of(2019, 1, 1));
        dto.setDataFim(java.time.LocalDate.of(2020, 12, 31));

        org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId());
        org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Desenvolvedor Júnior", dto.getCargo());
        org.junit.jupiter.api.Assertions.assertEquals("Empresa ABC", dto.getEmpresa());
        org.junit.jupiter.api.Assertions.assertEquals("Atuação em APIs", dto.getResumo());
        org.junit.jupiter.api.Assertions.assertEquals(java.time.LocalDate.of(2019, 1, 1), dto.getDataInicio());
        org.junit.jupiter.api.Assertions.assertEquals(java.time.LocalDate.of(2020, 12, 31), dto.getDataFim());
    }
}