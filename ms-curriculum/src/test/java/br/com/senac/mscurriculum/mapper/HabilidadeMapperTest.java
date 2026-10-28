package br.com.senac.mscurriculum.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HabilidadeMapper Tests")
class HabilidadeMapperTest {

    // ==================== toDTO Tests ====================

    @Test
    @DisplayName("Deve converter HabilidadeEntity para HabilidadeDTO com sucesso")
    void testToDTOSuccess() {
        // Arrange
        HabilidadeEntity entity = new HabilidadeEntity(
                1L,
                10L,
                "Java",
                "Avançado"
        );

        // Act
        HabilidadeDTO dto = HabilidadeMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(1L, dto.getId(), "ID deve ser igual");
        assertEquals(10L, dto.getIdCandidato(), "ID do candidato deve ser igual");
        assertEquals("Java", dto.getDescricao(), "Descrição deve ser igual");
        assertEquals("Avançado", dto.getNivel(), "Nível deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter entity nula para DTO")
    void testToDTOWithNullEntity() {
        // Act
        HabilidadeDTO dto = HabilidadeMapper.toDTO(null);

        // Assert
        assertNull(dto, "DTO deve ser nulo quando entity é nula");
    }

    @Test
    @DisplayName("Deve converter HabilidadeEntity com nível nulo para DTO")
    void testToDTOWithNullNivel() {
        // Arrange
        HabilidadeEntity entity = new HabilidadeEntity(
                1L,
                10L,
                "Python",
                null
        );

        // Act
        HabilidadeDTO dto = HabilidadeMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getNivel(), "Nível deve ser nulo");
        assertEquals("Python", dto.getDescricao(), "Descrição deve ser igual");
    }

    @Test
    @DisplayName("Deve converter HabilidadeEntity com descrição nula para DTO")
    void testToDTOWithNullDescricao() {
        // Arrange
        HabilidadeEntity entity = new HabilidadeEntity(
                2L,
                11L,
                null,
                "Intermediário"
        );

        // Act
        HabilidadeDTO dto = HabilidadeMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getDescricao(), "Descrição deve ser nula");
        assertEquals("Intermediário", dto.getNivel(), "Nível deve ser igual");
    }

    @Test
    @DisplayName("Deve converter HabilidadeEntity com id zero para DTO")
    void testToDTOWithZeroId() {
        // Arrange
        HabilidadeEntity entity = new HabilidadeEntity(
                0L,
                10L,
                "Spring Boot",
                "Avançado"
        );

        // Act
        HabilidadeDTO dto = HabilidadeMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(0L, dto.getId(), "ID deve ser zero");
        assertEquals("Spring Boot", dto.getDescricao(), "Descrição deve ser igual");
    }

    // ==================== toEntity Tests ====================

    @Test
    @DisplayName("Deve converter HabilidadeDTO para HabilidadeEntity com sucesso")
    void testToEntitySuccess() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO(
                1L,
                10L,
                "Java",
                "Avançado"
        );
        Long candidatoId = 20L;

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, candidatoId);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(1L, entity.getId(), "ID deve ser igual");
        assertEquals(20L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("Java", entity.getDescricao(), "Descrição deve ser igual");
        assertEquals("Avançado", entity.getNivel(), "Nível deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter DTO nulo para Entity")
    void testToEntityWithNullDTO() {
        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(null, 10L);

        // Assert
        assertNull(entity, "Entity deve ser nula quando DTO é nulo");
    }

    @Test
    @DisplayName("Deve usar candidatoId do parâmetro ao converter para Entity")
    void testToEntityWithDifferentCandidatoId() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO(
                1L,
                10L,
                "JavaScript",
                "Intermediário"
        );
        Long candidatoIdParametro = 999L;

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, candidatoIdParametro);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertEquals(999L, entity.getIdCandidato(), "ID do candidato deve ser o do parâmetro, não do DTO");
        assertEquals(1L, entity.getId(), "ID da habilidade deve vir do DTO");
    }

    @Test
    @DisplayName("Deve converter HabilidadeDTO com nível nulo para HabilidadeEntity")
    void testToEntityWithNullNivel() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO(
                1L,
                10L,
                "Python",
                null
        );

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, 20L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getNivel(), "Nível deve ser nulo");
        assertEquals("Python", entity.getDescricao(), "Descrição deve ser igual");
    }

    @Test
    @DisplayName("Deve converter HabilidadeDTO com descrição nula para HabilidadeEntity")
    void testToEntityWithNullDescricao() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO(
                2L,
                11L,
                null,
                "Avançado"
        );

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, 25L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getDescricao(), "Descrição deve ser nula");
        assertEquals("Avançado", entity.getNivel(), "Nível deve ser igual");
    }

    @Test
    @DisplayName("Deve converter HabilidadeDTO com valores mínimos necessários")
    void testToEntityWithMinimalData() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO();
        dto.setId(null);
        dto.setIdCandidato(null);
        dto.setDescricao("C#");
        dto.setNivel(null);

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, 5L);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getId(), "ID pode ser nulo");
        assertEquals(5L, entity.getIdCandidato(), "ID do candidato deve ser o passado como parâmetro");
        assertEquals("C#", entity.getDescricao(), "Descrição deve ser igual");
        assertNull(entity.getNivel(), "Nível deve ser nulo");
    }

    @Test
    @DisplayName("Deve converter HabilidadeDTO com candidatoId nulo do parâmetro")
    void testToEntityWithNullCandidatoIdParameter() {
        // Arrange
        HabilidadeDTO dto = new HabilidadeDTO(
                3L,
                15L,
                "Kotlin",
                "Intermediário"
        );

        // Act
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dto, null);

        // Assert
        assertNotNull(entity, "Entity não deve ser nula");
        assertNull(entity.getIdCandidato(), "ID do candidato deve ser nulo");
        assertEquals("Kotlin", entity.getDescricao(), "Descrição deve ser igual");
    }

    // ==================== Testes de Bidireção (DTO -> Entity -> DTO) ====================

    @Test
    @DisplayName("Deve converter corretamente em ambas as direções (DTO -> Entity -> DTO)")
    void testBidirectionalConversion() {
        // Arrange - DTO Original
        HabilidadeDTO dtoOriginal = new HabilidadeDTO(
                1L,
                10L,
                "Java",
                "Avançado"
        );

        // Act - Converter para Entity
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dtoOriginal, 20L);

        // Act - Converter Entity de volta para DTO
        HabilidadeDTO dtoConverted = HabilidadeMapper.toDTO(entity);

        // Assert - Verificar valores importantes
        assertNotNull(dtoConverted, "DTO convertido não deve ser nulo");
        assertEquals(dtoOriginal.getId(), dtoConverted.getId(), "ID deve ser preservado");
        assertEquals("Java", dtoConverted.getDescricao(), "Descrição deve ser preservada");
        assertEquals("Avançado", dtoConverted.getNivel(), "Nível deve ser preservado");
        assertEquals(20L, dtoConverted.getIdCandidato(), "ID do candidato deve ser o novo");
    }

    @Test
    @DisplayName("Deve converter corretamente em ambas as direções com nível nulo")
    void testBidirectionalConversionWithNullNivel() {
        // Arrange - DTO Original com nível nulo
        HabilidadeDTO dtoOriginal = new HabilidadeDTO(
                2L,
                11L,
                "Docker",
                null
        );

        // Act - Converter para Entity
        HabilidadeEntity entity = HabilidadeMapper.toEntity(dtoOriginal, 30L);

        // Act - Converter Entity de volta para DTO
        HabilidadeDTO dtoConverted = HabilidadeMapper.toDTO(entity);

        // Assert - Verificar que a conversão bidirecional funciona corretamente
        assertNotNull(dtoConverted, "DTO convertido não deve ser nulo");
        assertNull(dtoConverted.getNivel(), "Nível deve permanecer nulo");
        assertEquals("Docker", dtoConverted.getDescricao(), "Descrição deve ser preservada");
        assertEquals(30L, dtoConverted.getIdCandidato(), "ID do candidato deve ser preservado");
    }

    @Test
    @DisplayName("Deve converter múltiplas habilidades de forma independente")
    void testMultipleHabilitiesConversion() {
        // Arrange
        HabilidadeDTO dto1 = new HabilidadeDTO(1L, 10L, "Java", "Avançado");
        HabilidadeDTO dto2 = new HabilidadeDTO(2L, 10L, "Spring Boot", "Intermediário");
        HabilidadeDTO dto3 = new HabilidadeDTO(3L, 10L, "SQL", "Avançado");

        // Act - Converter primeira habilidade
        HabilidadeEntity entity1 = HabilidadeMapper.toEntity(dto1, 50L);
        HabilidadeDTO dtoConverted1 = HabilidadeMapper.toDTO(entity1);

        // Act - Converter segunda habilidade
        HabilidadeEntity entity2 = HabilidadeMapper.toEntity(dto2, 50L);
        HabilidadeDTO dtoConverted2 = HabilidadeMapper.toDTO(entity2);

        // Act - Converter terceira habilidade
        HabilidadeEntity entity3 = HabilidadeMapper.toEntity(dto3, 50L);
        HabilidadeDTO dtoConverted3 = HabilidadeMapper.toDTO(entity3);

        // Assert - Verificar que cada conversão funciona independentemente
        assertEquals("Java", dtoConverted1.getDescricao(), "Primeira habilidade deve ser Java");
        assertEquals("Spring Boot", dtoConverted2.getDescricao(), "Segunda habilidade deve ser Spring Boot");
        assertEquals("SQL", dtoConverted3.getDescricao(), "Terceira habilidade deve ser SQL");
        assertEquals(50L, dtoConverted1.getIdCandidato(), "Candidato ID deve ser o mesmo para todas");
        assertEquals(50L, dtoConverted2.getIdCandidato(), "Candidato ID deve ser o mesmo para todas");
        assertEquals(50L, dtoConverted3.getIdCandidato(), "Candidato ID deve ser o mesmo para todas");
    }
}