package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EducacaoMapper Tests")
class EducacaoMapperTest {

    // ==================== toDTO Tests ====================

    @Test
    @DisplayName("Deve converter EducacaoEntity para EducacaoDTO com sucesso")
    void testToDTOSuccess() {
        // Arrange
        EducacaoEntity entity = new EducacaoEntity(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                LocalDate.of(2016, 12, 31)
        );

        // Act
        EducacaoDTO dto = EducacaoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(1L, dto.getId(), "ID deve ser igual");
        assertEquals(10L, dto.getIdCandidato(), "ID do candidato deve ser igual");
        assertEquals("Análise e Desenvolvimento de Sistemas", dto.getCurso(), "Curso deve ser igual");
        assertEquals("Técnico", dto.getGrau(), "Grau deve ser igual");
        assertEquals("Senac", dto.getInstituicao(), "Instituição deve ser igual");
        assertEquals(LocalDate.of(2014, 1, 1), dto.getDataInicio(), "Data de início deve ser igual");
        assertEquals(LocalDate.of(2016, 12, 31), dto.getDataFim(), "Data de fim deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter entity nula para DTO")
    void testToDTOWithNullEntity() {
        // Act
        EducacaoDTO dto = EducacaoMapper.toDTO(null);

        // Assert
        assertNull(dto, "DTO deve ser nulo quando entity é nula");
    }

    @Test
    @DisplayName("Deve converter EducacaoEntity com dataFim nula para DTO")
    void testToDTOWithNullDataFim() {
        // Arrange
        EducacaoEntity entity = new EducacaoEntity(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                null
        );

        // Act
        EducacaoDTO dto = EducacaoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getDataFim(), "Data de fim deve ser nula");
        assertEquals(LocalDate.of(2014, 1, 1), dto.getDataInicio(), "Data de início deve ser igual");
    }

    // ==================== toEntity Tests ====================

    @Test
    @DisplayName("Deve converter EducacaoDTO para EducacaoEntity com sucesso")
    void testToEntitySuccess() {
        // Arrange
        EducacaoDTO dto = new EducacaoDTO(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                LocalDate.of(2016, 12, 31)
        );
        Long candidatoId = 20L;

        // Act
        EducacaoEntity entity = EducacaoMapper.toEntity(dto, candidatoId);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(1L, entity.getId(), "ID deve ser igual");
        assertEquals(20L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("Análise e Desenvolvimento de Sistemas", entity.getCurso(), "Curso deve ser igual");
        assertEquals("Técnico", entity.getGrau(), "Grau deve ser igual");
        assertEquals("Senac", entity.getInstituicao(), "Instituição deve ser igual");
        assertEquals(LocalDate.of(2014, 1, 1), entity.getDataInicio(), "Data de início deve ser igual");
        assertEquals(LocalDate.of(2016, 12, 31), entity.getDataFim(), "Data de fim deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter DTO nulo para Entity")
    void testToEntityWithNullDTO() {
        // Act
        EducacaoEntity entity = EducacaoMapper.toEntity(null, 10L);

        // Assert
        assertNull(entity, "Entity deve ser nula quando DTO é nulo");
    }

    @Test
    @DisplayName("Deve usar candidatoId do parâmetro ao converter para Entity")
    void testToEntityWithDifferentCandidatoId() {
        // Arrange
        EducacaoDTO dto = new EducacaoDTO(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                LocalDate.of(2016, 12, 31)
        );
        Long candidatoIdParametro = 999L;

        // Act
        EducacaoEntity entity = EducacaoMapper.toEntity(dto, candidatoIdParametro);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(999L, entity.getIdCandidato(), "ID do candidato deve ser o do parâmetro, não do DTO");
        assertEquals(1L, entity.getId(), "ID da educação deve vir do DTO");
    }

    @Test
    @DisplayName("Deve converter EducacaoDTO com dataFim nula para EducacaoEntity")
    void testToEntityWithNullDataFim() {
        // Arrange
        EducacaoDTO dto = new EducacaoDTO(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                null
        );

        // Act
        EducacaoEntity entity = EducacaoMapper.toEntity(dto, 20L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getDataFim(), "Data de fim deve ser nula");
        assertEquals(LocalDate.of(2014, 1, 1), entity.getDataInicio(), "Data de início deve ser igual");
    }

    @Test
    @DisplayName("Deve converter EducacaoDTO com valores mínimos necessários")
    void testToEntityWithMinimalData() {
        // Arrange
        EducacaoDTO dto = new EducacaoDTO();
        dto.setId(null);
        dto.setIdCandidato(null);
        dto.setCurso("Curso X");
        dto.setGrau("Graduação");
        dto.setInstituicao("Instituição Y");
        dto.setDataInicio(LocalDate.now());
        dto.setDataFim(null);

        // Act
        EducacaoEntity entity = EducacaoMapper.toEntity(dto, 5L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getId(), "ID pode ser nulo");
        assertEquals(5L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("Curso X", entity.getCurso(), "Curso deve ser igual");
    }

    // ==================== Testes de Bidireção (DTO -> Entity -> DTO) ====================

    @Test
    @DisplayName("Deve converter corretamente em ambas as direções (DTO -> Entity -> DTO)")
    void testBidirectionalConversion() {
        // Arrange - DTO Original
        EducacaoDTO dtoOriginal = new EducacaoDTO(
                1L,
                10L,
                "Análise e Desenvolvimento de Sistemas",
                "Técnico",
                "Senac",
                LocalDate.of(2014, 1, 1),
                LocalDate.of(2016, 12, 31)
        );

        // Act - Converter para Entity
        EducacaoEntity entity = EducacaoMapper.toEntity(dtoOriginal, 20L);

        // Act - Converter Entity de volta para DTO
        EducacaoDTO dtoConverted = EducacaoMapper.toDTO(entity);

        // Assert - Verificar valores importantes
        assertNotNull(dtoConverted, "DTO convertido não deve ser nulo");
        assertEquals(dtoOriginal.getId(), dtoConverted.getId(), "ID deve ser preservado");
        assertEquals("Análise e Desenvolvimento de Sistemas", dtoConverted.getCurso(), "Curso deve ser preservado");
        assertEquals("Técnico", dtoConverted.getGrau(), "Grau deve ser preservado");
        assertEquals("Senac", dtoConverted.getInstituicao(), "Instituição deve ser preservada");
        assertEquals(LocalDate.of(2014, 1, 1), dtoConverted.getDataInicio(), "Data de início deve ser preservada");
        assertEquals(LocalDate.of(2016, 12, 31), dtoConverted.getDataFim(), "Data de fim deve ser preservada");
        assertEquals(20L, dtoConverted.getIdCandidato(), "ID do candidato deve ser o novo");
    }
}