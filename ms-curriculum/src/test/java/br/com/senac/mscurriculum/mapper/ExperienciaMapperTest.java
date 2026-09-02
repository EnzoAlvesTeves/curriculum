package br.com.senac.mscurriculum.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("ExperienciaMapper Tests")
class ExperienciaMapperTest {

    // ==================== toDTO Tests ====================

    @Test
    @DisplayName("Deve converter ExperienciaEntity para ExperienciaDTO com sucesso")
    void testToDTOSuccess() {
        // Arrange
        ExperienciaEntity entity = new ExperienciaEntity(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 12, 31)
        );

        // Act
        ExperienciaDTO dto = ExperienciaMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(1L, dto.getId(), "ID deve ser igual");
        assertEquals(10L, dto.getIdCandidato(), "ID do candidato deve ser igual");
        assertEquals("Desenvolvedor Pleno", dto.getCargo(), "Cargo deve ser igual");
        assertEquals("Empresa XYZ", dto.getEmpresa(), "Empresa deve ser igual");
        assertEquals("Desenvolvimento de APIs Java", dto.getResumo(), "Resumo deve ser igual");
        assertEquals(LocalDate.of(2019, 1, 1), dto.getDataInicio(), "Data de início deve ser igual");
        assertEquals(LocalDate.of(2020, 12, 31), dto.getDataFim(), "Data de fim deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter entity nula para DTO")
    void testToDTOWithNullEntity() {
        // Act
        ExperienciaDTO dto = ExperienciaMapper.toDTO(null);

        // Assert
        assertNull(dto, "DTO deve ser nulo quando entity é nula");
    }

    @Test
    @DisplayName("Deve converter ExperienciaEntity com dataFim nula para DTO")
    void testToDTOWithNullDataFim() {
        // Arrange
        ExperienciaEntity entity = new ExperienciaEntity(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                null
        );

        // Act
        ExperienciaDTO dto = ExperienciaMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getDataFim(), "Data de fim deve ser nula");
        assertEquals(LocalDate.of(2019, 1, 1), dto.getDataInicio(), "Data de início deve ser igual");
    }

    @Test
    @DisplayName("Deve converter ExperienciaEntity com resumo nulo para DTO")
    void testToDTOWithNullResumo() {
        // Arrange
        ExperienciaEntity entity = new ExperienciaEntity(
                2L,
                11L,
                "Analista de Sistemas",
                "Empresa ABC",
                null,
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2021, 12, 31)
        );

        // Act
        ExperienciaDTO dto = ExperienciaMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getResumo(), "Resumo deve ser nulo");
        assertEquals("Analista de Sistemas", dto.getCargo(), "Cargo deve ser igual");
    }

    // ==================== toEntity Tests ====================

    @Test
    @DisplayName("Deve converter ExperienciaDTO para ExperienciaEntity com sucesso")
    void testToEntitySuccess() {
        // Arrange
        ExperienciaDTO dto = new ExperienciaDTO(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 12, 31)
        );
        Long candidatoId = 20L;

        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dto, candidatoId);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(1L, entity.getId(), "ID deve ser igual");
        assertEquals(20L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("Desenvolvedor Pleno", entity.getCargo(), "Cargo deve ser igual");
        assertEquals("Empresa XYZ", entity.getEmpresa(), "Empresa deve ser igual");
        assertEquals("Desenvolvimento de APIs Java", entity.getResumo(), "Resumo deve ser igual");
        assertEquals(LocalDate.of(2019, 1, 1), entity.getDataInicio(), "Data de início deve ser igual");
        assertEquals(LocalDate.of(2020, 12, 31), entity.getDataFim(), "Data de fim deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter DTO nulo para Entity")
    void testToEntityWithNullDTO() {
        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(null, 10L);

        // Assert
        assertNull(entity, "Entity deve ser nula quando DTO é nulo");
    }

    @Test
    @DisplayName("Deve usar candidatoId do parâmetro ao converter para Entity")
    void testToEntityWithDifferentCandidatoId() {
        // Arrange
        ExperienciaDTO dto = new ExperienciaDTO(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 12, 31)
        );
        Long candidatoIdParametro = 999L;

        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dto, candidatoIdParametro);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(999L, entity.getIdCandidato(), "ID do candidato deve ser o do parâmetro, não do DTO");
        assertEquals(1L, entity.getId(), "ID da experiência deve vir do DTO");
    }

    @Test
    @DisplayName("Deve converter ExperienciaDTO com dataFim nula para ExperienciaEntity")
    void testToEntityWithNullDataFim() {
        // Arrange
        ExperienciaDTO dto = new ExperienciaDTO(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                null
        );

        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dto, 20L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getDataFim(), "Data de fim deve ser nula");
        assertEquals(LocalDate.of(2019, 1, 1), entity.getDataInicio(), "Data de início deve ser igual");
    }

    @Test
    @DisplayName("Deve converter ExperienciaDTO com resumo nulo para ExperienciaEntity")
    void testToEntityWithNullResumo() {
        // Arrange
        ExperienciaDTO dto = new ExperienciaDTO(
                2L,
                11L,
                "Analista de Sistemas",
                "Empresa ABC",
                null,
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2021, 12, 31)
        );

        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dto, 25L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getResumo(), "Resumo deve ser nulo");
        assertEquals("Analista de Sistemas", entity.getCargo(), "Cargo deve ser igual");
    }

    @Test
    @DisplayName("Deve converter ExperienciaDTO com valores mínimos necessários")
    void testToEntityWithMinimalData() {
        // Arrange
        ExperienciaDTO dto = new ExperienciaDTO();
        dto.setId(null);
        dto.setIdCandidato(null);
        dto.setCargo("Estagiário");
        dto.setEmpresa("Empresa Start");
        dto.setResumo(null);
        dto.setDataInicio(LocalDate.now());
        dto.setDataFim(null);

        // Act
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dto, 5L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getId(), "ID pode ser nulo");
        assertEquals(5L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("Estagiário", entity.getCargo(), "Cargo deve ser igual");
    }

    // ==================== Testes de Bidireção (DTO -> Entity -> DTO) ====================

    @Test
    @DisplayName("Deve converter corretamente em ambas as direções (DTO -> Entity -> DTO)")
    void testBidirectionalConversion() {
        // Arrange - DTO Original
        ExperienciaDTO dtoOriginal = new ExperienciaDTO(
                1L,
                10L,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs Java",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 12, 31)
        );

        // Act - Converter para Entity
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dtoOriginal, 20L);

        // Act - Converter Entity de volta para DTO
        ExperienciaDTO dtoConverted = ExperienciaMapper.toDTO(entity);

        // Assert - Verificar valores importantes
        assertNotNull(dtoConverted, "DTO convertido não deve ser nulo");
        assertEquals(dtoOriginal.getId(), dtoConverted.getId(), "ID deve ser preservado");
        assertEquals("Desenvolvedor Pleno", dtoConverted.getCargo(), "Cargo deve ser preservado");
        assertEquals("Empresa XYZ", dtoConverted.getEmpresa(), "Empresa deve ser preservada");
        assertEquals("Desenvolvimento de APIs Java", dtoConverted.getResumo(), "Resumo deve ser preservado");
        assertEquals(LocalDate.of(2019, 1, 1), dtoConverted.getDataInicio(), "Data de início deve ser preservada");
        assertEquals(LocalDate.of(2020, 12, 31), dtoConverted.getDataFim(), "Data de fim deve ser preservada");
        assertEquals(20L, dtoConverted.getIdCandidato(), "ID do candidato deve ser o novo");
    }

    @Test
    @DisplayName("Deve converter corretamente com experiência em andamento (dataFim nula)")
    void testBidirectionalConversionWithOngoingExperience() {
        // Arrange - DTO Original com experiência em andamento
        ExperienciaDTO dtoOriginal = new ExperienciaDTO(
                3L,
                15L,
                "Desenvolvedor Sênior",
                "Empresa DEF",
                "Liderança de equipe e arquitetura",
                LocalDate.of(2022, 1, 15),
                null
        );

        // Act - Converter para Entity
        ExperienciaEntity entity = ExperienciaMapper.toEntity(dtoOriginal, 30L);

        // Act - Converter Entity de volta para DTO
        ExperienciaDTO dtoConverted = ExperienciaMapper.toDTO(entity);

        // Assert - Verificar que a conversão bidirecional funciona corretamente
        assertNotNull(dtoConverted, "DTO convertido não deve ser nulo");
        assertNull(dtoConverted.getDataFim(), "Data de fim deve permanecer nula");
        assertEquals("Desenvolvedor Sênior", dtoConverted.getCargo(), "Cargo deve ser preservado");
        assertEquals(LocalDate.of(2022, 1, 15), dtoConverted.getDataInicio(), "Data de início deve ser preservada");
    }
}