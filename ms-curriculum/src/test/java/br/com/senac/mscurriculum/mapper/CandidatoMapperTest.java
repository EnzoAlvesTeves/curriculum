package br.com.senac.mscurriculum.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DisplayName("CandidatoMapper Tests")
class CandidatoMapperTest {

    // ==================== toDTO Tests ====================

    @Test
    @DisplayName("Deve converter CandidatoEntity para CandidatoDTO com sucesso")
    void testToDTOSuccess() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                1L,
                "João da Silva",
                "joao@email.com",
                "MASCULINO",
                "11999999999",
                LocalDate.of(1989, 12, 31),
                "Desenvolvedor Java com experiência em microserviços",
                10L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(1L, dto.getId(), "ID deve ser igual");
        assertEquals(10L, dto.getIdUsuario(), "ID do usuário deve ser igual");
        assertEquals("João da Silva", dto.getNome(), "Nome deve ser igual");
        assertEquals("joao@email.com", dto.getEmail(), "Email deve ser igual");
        assertEquals("MASCULINO", dto.getSexo(), "Sexo deve ser igual");
        assertEquals("11999999999", dto.getTelefone(), "Telefone deve ser igual");
        assertEquals(LocalDate.of(1989, 12, 31), dto.getDataNascimento(), "Data de nascimento deve ser igual");
        assertEquals("Desenvolvedor Java com experiência em microserviços", dto.getResumoProfissional(), "Resumo profissional deve ser igual");
    }

    @Test
    @DisplayName("Deve retornar null ao converter entity nula para DTO")
    void testToDTOWithNullEntity() {
        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(null);

        // Assert
        assertNull(dto, "DTO deve ser nulo quando entity é nula");
    }

    @Test
    @DisplayName("Deve converter CandidatoEntity com sexo nulo para DTO")
    void testToDTOWithNullSexo() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                1L,
                "Maria Silva",
                "maria@email.com",
                null,
                "11988888888",
                LocalDate.of(1990, 5, 15),
                "Analista de Sistemas",
                10L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getSexo(), "Sexo deve ser nulo");
        assertEquals("Maria Silva", dto.getNome(), "Nome deve ser igual");
    }

    @Test
    @DisplayName("Deve converter CandidatoEntity com telefone nulo para DTO")
    void testToDTOWithNullTelefone() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                2L,
                "Pedro Santos",
                "pedro@email.com",
                "MASCULINO",
                null,
                LocalDate.of(1985, 8, 22),
                "Desenvolvedor Pleno",
                11L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getTelefone(), "Telefone deve ser nulo");
        assertEquals(11L, dto.getIdUsuario(), "ID do usuário deve ser igual");
    }

    @Test
    @DisplayName("Deve converter CandidatoEntity com resumoProfissional nulo para DTO")
    void testToDTOWithNullResumoProfissional() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                3L,
                "Ana Costa",
                "ana@email.com",
                "FEMININO",
                "11977777777",
                LocalDate.of(1992, 3, 10),
                null,
                12L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertNull(dto.getResumoProfissional(), "Resumo profissional deve ser nulo");
        assertEquals("Ana Costa", dto.getNome(), "Nome deve ser igual");
    }

    @Test
    @DisplayName("Deve converter CandidatoEntity com todos os campos preenchidos")
    void testToDTOWithAllFieldsFilled() {
        // Arrange
        LocalDate dataNascimento = LocalDate.of(1988, 6, 15);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        CandidatoEntity entity = new CandidatoEntity(
                5L,
                "Carlos Oliveira",
                "carlos@email.com",
                "MASCULINO",
                "11966666666",
                dataNascimento,
                "Arquiteto de Software com 10 anos de experiência",
                15L,
                createdAt,
                updatedAt
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(5L, dto.getId(), "ID deve ser igual");
        assertEquals(15L, dto.getIdUsuario(), "ID do usuário deve ser igual");
        assertEquals("Carlos Oliveira", dto.getNome(), "Nome deve ser igual");
        assertEquals("carlos@email.com", dto.getEmail(), "Email deve ser igual");
        assertEquals("MASCULINO", dto.getSexo(), "Sexo deve ser igual");
        assertEquals("11966666666", dto.getTelefone(), "Telefone deve ser igual");
        assertEquals(dataNascimento, dto.getDataNascimento(), "Data de nascimento deve ser igual");
        assertEquals("Arquiteto de Software com 10 anos de experiência", dto.getResumoProfissional(), "Resumo profissional deve ser igual");
    }

    @Test
    @DisplayName("Deve converter CandidatoEntity com vários campos nulos para DTO")
    void testToDTOWithMultipleNullFields() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                4L,
                "Fernanda Gomes",
                "fernanda@email.com",
                null,
                null,
                LocalDate.of(1995, 11, 20),
                null,
                13L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(4L, dto.getId(), "ID deve ser igual");
        assertEquals("Fernanda Gomes", dto.getNome(), "Nome deve ser igual");
        assertEquals("fernanda@email.com", dto.getEmail(), "Email deve ser igual");
        assertNull(dto.getSexo(), "Sexo deve ser nulo");
        assertNull(dto.getTelefone(), "Telefone deve ser nulo");
        assertNull(dto.getResumoProfissional(), "Resumo profissional deve ser nulo");
    }

    @Test
    @DisplayName("Deve não modificar campos de auditoria na conversão para DTO")
    void testToDTODoesNotIncludeAuditFields() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        CandidatoEntity entity = new CandidatoEntity(
                6L,
                "Lucas Martins",
                "lucas@email.com",
                "MASCULINO",
                "11955555555",
                LocalDate.of(1991, 7, 25),
                "Desenvolvedor Backend",
                16L,
                createdAt,
                updatedAt
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        // Verificar que os campos de auditoria não foram incluídos no DTO
        assertNull(dto.getEndereco(), "Endereço deve ser nulo no DTO convertido");
        assertTrue(dto.getEducacoes().isEmpty(), "Lista de educações deve estar vazia");
        assertTrue(dto.getExperiencias().isEmpty(), "Lista de experiências deve estar vazia");
        assertTrue(dto.getHabilidades().isEmpty(), "Lista de habilidades deve estar vazia");
    }

    @Test
    @DisplayName("Deve converter múltiplos candidatos de forma independente")
    void testMultipleCandidatesConversion() {
        // Arrange
        CandidatoEntity entity1 = new CandidatoEntity(
                1L,
                "João da Silva",
                "joao@email.com",
                "MASCULINO",
                "11999999999",
                LocalDate.of(1989, 12, 31),
                "Desenvolvedor Java",
                10L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CandidatoEntity entity2 = new CandidatoEntity(
                2L,
                "Maria Santos",
                "maria@email.com",
                "FEMININO",
                "11988888888",
                LocalDate.of(1990, 5, 15),
                "Analista de Sistemas",
                11L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto1 = CandidatoMapper.toDTO(entity1);
        CandidatoDTO dto2 = CandidatoMapper.toDTO(entity2);

        // Assert
        assertNotNull(dto1, "Primeiro DTO não deve ser nulo");
        assertNotNull(dto2, "Segundo DTO não deve ser nulo");
        assertEquals("João da Silva", dto1.getNome(), "Primeiro candidato deve ser João");
        assertEquals("Maria Santos", dto2.getNome(), "Segundo candidato deve ser Maria");
        assertEquals("MASCULINO", dto1.getSexo(), "Sexo do primeiro candidato deve ser MASCULINO");
        assertEquals("FEMININO", dto2.getSexo(), "Sexo do segundo candidato deve ser FEMININO");
        assertNotEquals(dto1.getId(), dto2.getId(), "IDs dos candidatos devem ser diferentes");
    }

    @Test
    @DisplayName("Deve preservar valores vazios em strings (não nulos)")
    void testToDTOWithEmptyStrings() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                7L,
                "Roberto Lima",
                "roberto@email.com",
                "",
                "",
                LocalDate.of(1987, 9, 5),
                "",
                17L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals("", dto.getSexo(), "Sexo vazio deve ser preservado");
        assertEquals("", dto.getTelefone(), "Telefone vazio deve ser preservado");
        assertEquals("", dto.getResumoProfissional(), "Resumo profissional vazio deve ser preservado");
    }

    @Test
    @DisplayName("Deve converter candidato recém-criado (sem updated_at)")
    void testToDTOWithNullUpdatedAt() {
        // Arrange
        CandidatoEntity entity = new CandidatoEntity(
                8L,
                "Juliana Ferreira",
                "juliana@email.com",
                "FEMININO",
                "11944444444",
                LocalDate.of(1994, 2, 28),
                "Gerente de Projetos",
                18L,
                LocalDateTime.now(),
                null
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals("Juliana Ferreira", dto.getNome(), "Nome deve ser igual");
        assertEquals(18L, dto.getIdUsuario(), "ID do usuário deve ser igual");
    }

    @Test
    @DisplayName("Deve converter candidato com data de nascimento recente")
    void testToDTOWithRecentBirthDate() {
        // Arrange
        LocalDate dataNascimento = LocalDate.of(2005, 1, 1);
        CandidatoEntity entity = new CandidatoEntity(
                9L,
                "Gabriel Souza",
                "gabriel@email.com",
                "MASCULINO",
                "11933333333",
                dataNascimento,
                "Estagiário em Desenvolvimento",
                19L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(dataNascimento, dto.getDataNascimento(), "Data de nascimento deve ser igual");
        assertEquals("Gabriel Souza", dto.getNome(), "Nome deve ser igual");
    }

    @Test
    @DisplayName("Deve converter candidato com data de nascimento antiga")
    void testToDTOWithOldBirthDate() {
        // Arrange
        LocalDate dataNascimento = LocalDate.of(1960, 12, 25);
        CandidatoEntity entity = new CandidatoEntity(
                10L,
                "José Pereira",
                "jose@email.com",
                "MASCULINO",
                "11922222222",
                dataNascimento,
                "Consultor Sênior",
                20L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        CandidatoDTO dto = CandidatoMapper.toDTO(entity);

        // Assert
        assertNotNull(dto, "DTO não deve ser nulo");
        assertEquals(dataNascimento, dto.getDataNascimento(), "Data de nascimento deve ser igual");
        assertEquals("José Pereira", dto.getNome(), "Nome deve ser igual");
    }
}