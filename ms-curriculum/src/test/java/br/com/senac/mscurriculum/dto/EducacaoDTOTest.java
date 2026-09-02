package br.com.senac.mscurriculum.dto;

import java.time.LocalDate;

class EducacaoDTOTest {

    @org.junit.jupiter.api.Test
    void testConstrutorVazio() {
        EducacaoDTO dto = new EducacaoDTO();

        org.junit.jupiter.api.Assertions.assertNull(dto.getId());
        org.junit.jupiter.api.Assertions.assertNull(dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertNull(dto.getCurso());
        org.junit.jupiter.api.Assertions.assertNull(dto.getGrau());
        org.junit.jupiter.api.Assertions.assertNull(dto.getInstituicao());
        org.junit.jupiter.api.Assertions.assertNull(dto.getDataInicio());
        org.junit.jupiter.api.Assertions.assertNull(dto.getDataFim());
    }

    @org.junit.jupiter.api.Test
    void testConstrutorCompleto() {
        java.time.LocalDate dataInicio = java.time.LocalDate.of(2014, 1, 1);
        java.time.LocalDate dataFim = java.time.LocalDate.of(2016, 12, 31);

        EducacaoDTO dto = new EducacaoDTO(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                dataInicio,
                dataFim
        );

        org.junit.jupiter.api.Assertions.assertAll(
                () -> org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Análise e Desenvolvimento de Sistemas", dto.getCurso()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Técnico", dto.getGrau()),
                () -> org.junit.jupiter.api.Assertions.assertEquals("Senac", dto.getInstituicao()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(dataInicio, dto.getDataInicio()),
                () -> org.junit.jupiter.api.Assertions.assertEquals(dataFim, dto.getDataFim())
        );
    }

    @org.junit.jupiter.api.Test
    void testSetters() {
        EducacaoDTO dto = new EducacaoDTO();

        dto.setId(1L);
        dto.setIdCandidato(10L);
        dto.setCurso("Curso X");
        dto.setGrau("Graduação");
        dto.setInstituicao("Instituição Y");
        dto.setDataInicio(LocalDate.of(2020, 1, 1));
        dto.setDataFim(LocalDate.of(2021, 12, 31));

        org.junit.jupiter.api.Assertions.assertEquals(1L, dto.getId());
        org.junit.jupiter.api.Assertions.assertEquals(10L, dto.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Curso X", dto.getCurso());
        org.junit.jupiter.api.Assertions.assertEquals("Graduação", dto.getGrau());
        org.junit.jupiter.api.Assertions.assertEquals("Instituição Y", dto.getInstituicao());
        org.junit.jupiter.api.Assertions.assertEquals(java.time.LocalDate.of(2020, 1, 1), dto.getDataInicio());
        org.junit.jupiter.api.Assertions.assertEquals(java.time.LocalDate.of(2021, 12, 31), dto.getDataFim());
    }
}