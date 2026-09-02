package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoMapperTest {

    private static final Long ID = 1L;
    private static final Long CANDIDATO_ID = 100L;
    private static final String RUA = "Rua A";
    private static final String NUMERO = "123";
    private static final String COMPLEMENTO = "Apto 1";
    private static final String CIDADE = "São Paulo";
    private static final String ESTADO = "SP";
    private static final String CEP = "01000-000";
    private static final String BAIRRO = "Centro";
    private static final BigDecimal LATITUDE = new BigDecimal("-23.69");
    private static final BigDecimal LONGITUDE = new BigDecimal("-46.62");

    @Test
    void testToDTOWithValidEntity() {
        // Arrange
        EnderecoEntity entity = createEnderecoEntity();

        // Act
        EnderecoDTO result = EnderecoMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(CANDIDATO_ID, result.getIdCandidato());
        assertEquals(RUA, result.getRua());
        assertEquals(NUMERO, result.getNumero());
        assertEquals(COMPLEMENTO, result.getComplemento());
        assertEquals(CIDADE, result.getCidade());
        assertEquals(ESTADO, result.getEstado());
        assertEquals(CEP, result.getCep());
        assertEquals(BAIRRO, result.getBairro());
        assertEquals(LATITUDE, result.getLatitude());
        assertEquals(LONGITUDE, result.getLongitude());
    }

    @Test
    void testToDTOWithNullEntity() {
        // Act
        EnderecoDTO result = EnderecoMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToDTOWithEntityHavingNullFields() {
        // Arrange
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(ID);
        entity.setIdCandidato(CANDIDATO_ID);

        // Act
        EnderecoDTO result = EnderecoMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(CANDIDATO_ID, result.getIdCandidato());
        assertNull(result.getRua());
        assertNull(result.getNumero());
        assertNull(result.getComplemento());
        assertNull(result.getCidade());
        assertNull(result.getEstado());
        assertNull(result.getCep());
        assertNull(result.getBairro());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
    }

    @Test
    void testToEntityWithValidDTO() {
        // Arrange
        EnderecoDTO dto = createEnderecoDTO();

        // Act
        EnderecoEntity result = EnderecoMapper.toEntity(dto, CANDIDATO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(CANDIDATO_ID, result.getIdCandidato());
        assertEquals(RUA, result.getRua());
        assertEquals(NUMERO, result.getNumero());
        assertEquals(COMPLEMENTO, result.getComplemento());
        assertEquals(CIDADE, result.getCidade());
        assertEquals(ESTADO, result.getEstado());
        assertEquals(CEP, result.getCep());
        assertEquals(BAIRRO, result.getBairro());
        assertEquals(LATITUDE, result.getLatitude());
        assertEquals(LONGITUDE, result.getLongitude());
    }

    @Test
    void testToEntityWithNullDTO() {
        // Act
        EnderecoEntity result = EnderecoMapper.toEntity(null, CANDIDATO_ID);

        // Assert
        assertNull(result);
    }

    @Test
    void testToEntityWithDifferentCandidatoId() {
        // Arrange
        EnderecoDTO dto = createEnderecoDTO();
        Long differentCandidatoId = 200L;

        // Act
        EnderecoEntity result = EnderecoMapper.toEntity(dto, differentCandidatoId);

        // Assert
        assertNotNull(result);
        assertEquals(differentCandidatoId, result.getIdCandidato());
        assertEquals(ID, result.getId());
    }

    @Test
    void testToEntityWithDTOHavingNullFields() {
        // Arrange
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(ID);

        // Act
        EnderecoEntity result = EnderecoMapper.toEntity(dto, CANDIDATO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(CANDIDATO_ID, result.getIdCandidato());
        assertNull(result.getRua());
        assertNull(result.getNumero());
        assertNull(result.getComplemento());
        assertNull(result.getCidade());
        assertNull(result.getEstado());
        assertNull(result.getCep());
        assertNull(result.getBairro());
        assertNull(result.getLatitude());
        assertNull(result.getLongitude());
    }

    @Test
    void testMapperConstructor() {
        // Assert that constructor is private (not instantiable)
        assertThrows(Exception.class, () -> {
            var constructor = EnderecoMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void testRoundTripConversion() {
        // Arrange
        EnderecoDTO originalDTO = createEnderecoDTO();
        Long candidatoId = 150L;

        // Act
        EnderecoEntity entity = EnderecoMapper.toEntity(originalDTO, candidatoId);
        EnderecoDTO resultDTO = EnderecoMapper.toDTO(entity);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(originalDTO.getId(), resultDTO.getId());
        assertEquals(originalDTO.getRua(), resultDTO.getRua());
        assertEquals(originalDTO.getNumero(), resultDTO.getNumero());
        assertEquals(originalDTO.getComplemento(), resultDTO.getComplemento());
        assertEquals(originalDTO.getCidade(), resultDTO.getCidade());
        assertEquals(originalDTO.getEstado(), resultDTO.getEstado());
        assertEquals(originalDTO.getCep(), resultDTO.getCep());
        assertEquals(originalDTO.getBairro(), resultDTO.getBairro());
        assertEquals(originalDTO.getLatitude(), resultDTO.getLatitude());
        assertEquals(originalDTO.getLongitude(), resultDTO.getLongitude());
        assertEquals(candidatoId, resultDTO.getIdCandidato());
    }

    // Helper methods
    private EnderecoEntity createEnderecoEntity() {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(ID);
        entity.setIdCandidato(CANDIDATO_ID);
        entity.setRua(RUA);
        entity.setNumero(NUMERO);
        entity.setComplemento(COMPLEMENTO);
        entity.setCidade(CIDADE);
        entity.setEstado(ESTADO);
        entity.setCep(CEP);
        entity.setBairro(BAIRRO);
        entity.setLatitude(LATITUDE);
        entity.setLongitude(LONGITUDE);
        return entity;
    }

    private EnderecoDTO createEnderecoDTO() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(ID);
        dto.setIdCandidato(CANDIDATO_ID);
        dto.setRua(RUA);
        dto.setNumero(NUMERO);
        dto.setComplemento(COMPLEMENTO);
        dto.setCidade(CIDADE);
        dto.setEstado(ESTADO);
        dto.setCep(CEP);
        dto.setBairro(BAIRRO);
        dto.setLatitude(LATITUDE);
        dto.setLongitude(LONGITUDE);
        return dto;
    }
}