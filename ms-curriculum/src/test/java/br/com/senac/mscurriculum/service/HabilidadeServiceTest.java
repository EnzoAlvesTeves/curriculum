package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabilidadeServiceTest {

    @Mock
    private HabilidadeRepository habilidadeRepository;

    @Mock
    private CandidatoAuthorizationService candidatoAuthorizationService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private HabilidadeService habilidadeService;

    private static final Long CANDIDATO_ID = 1L;
    private static final Long HABILIDADE_ID = 100L;
    private static final String DESCRICAO = "Java";
    private static final String NIVEL = "Avançado";
    private static final String DESCRICAO_ATUALIZADA = "Spring Boot";
    private static final String NIVEL_ATUALIZADO = "Intermediário";

    @BeforeEach
    void setUp() {
        reset(habilidadeRepository, candidatoAuthorizationService);
    }

    // ==================== TESTES DE CADASTRO ====================

    @Test
    void testCadastrarComAutorizacaoComSucesso() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO, NIVEL);
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entity);

        // Act
        HabilidadeDTO result = habilidadeService.cadastrar(CANDIDATO_ID, dto, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(HABILIDADE_ID, result.getId());
        assertEquals(CANDIDATO_ID, result.getIdCandidato());
        assertEquals(DESCRICAO, result.getDescricao());
        assertEquals(NIVEL, result.getNivel());

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
    }

    @Test
    void testCadastrarComAutorizacaoFalhaAutorizacao() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO, NIVEL);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.cadastrar(CANDIDATO_ID, dto, jwt)
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, never()).save(any());
    }

    @Test
    void testCadastrarSemAutorizacaoComSucesso() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO, NIVEL);
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL);

        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entity);

        // Act
        HabilidadeDTO result = habilidadeService.cadastrar(CANDIDATO_ID, dto);

        // Assert
        assertNotNull(result);
        assertEquals(HABILIDADE_ID, result.getId());
        assertEquals(DESCRICAO, result.getDescricao());

        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
        verify(candidatoAuthorizationService, never()).validarAcessoDoUsuario(anyLong(), any());
    }

    @Test
    void testCadastrarComDescricaoVazia() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, "", NIVEL);
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, "", NIVEL);

        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entity);

        // Act
        HabilidadeDTO result = habilidadeService.cadastrar(CANDIDATO_ID, dto);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getDescricao());

        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
    }

    @Test
    void testCadastrarComNivelNull() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO, null);
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, null);

        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entity);

        // Act
        HabilidadeDTO result = habilidadeService.cadastrar(CANDIDATO_ID, dto);

        // Assert
        assertNotNull(result);
        assertNull(result.getNivel());

        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
    }

    // ==================== TESTES DE ALTERAÇÃO ====================

    @Test
    void testAlterarComSucesso() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO_ATUALIZADA, NIVEL_ATUALIZADO);
        HabilidadeEntity entityExistente = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL);
        HabilidadeEntity entityAtualizada = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO_ATUALIZADA, NIVEL_ATUALIZADO);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.of(entityExistente));
        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entityAtualizada);

        // Act
        HabilidadeDTO result = habilidadeService.alterar(CANDIDATO_ID, HABILIDADE_ID, dto, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(DESCRICAO_ATUALIZADA, result.getDescricao());
        assertEquals(NIVEL_ATUALIZADO, result.getNivel());

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
    }

    @Test
    void testAlterarHabilidadeNaoEncontrada() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO_ATUALIZADA, NIVEL_ATUALIZADO);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.alterar(CANDIDATO_ID, HABILIDADE_ID, dto, jwt),
                "Habilidade não encontrada para o ID: " + HABILIDADE_ID
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, never()).save(any());
    }

    @Test
    void testAlterarHabilidadeNaoPertenceAoCandidato() {
        // Arrange
        Long outroCandidatoId = 999L;
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO_ATUALIZADA, NIVEL_ATUALIZADO);
        HabilidadeEntity entityExistente = criarHabilidadeEntity(HABILIDADE_ID, outroCandidatoId, DESCRICAO, NIVEL);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.of(entityExistente));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.alterar(CANDIDATO_ID, HABILIDADE_ID, dto, jwt),
                "Habilidade não pertence ao candidato ID: " + CANDIDATO_ID
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, never()).save(any());
    }

    @Test
    void testAlterarFalhaAutorizacao() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO_ATUALIZADA, NIVEL_ATUALIZADO);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.alterar(CANDIDATO_ID, HABILIDADE_ID, dto, jwt)
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, never()).findById(anyLong());
    }

    @Test
    void testAlterarApenasDescricao() {
        // Arrange
        HabilidadeDTO dto = criarHabilidadeDTO(null, null, DESCRICAO_ATUALIZADA, NIVEL);
        HabilidadeEntity entityExistente = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL);
        HabilidadeEntity entityAtualizada = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO_ATUALIZADA, NIVEL);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.of(entityExistente));
        when(habilidadeRepository.save(any(HabilidadeEntity.class))).thenReturn(entityAtualizada);

        // Act
        HabilidadeDTO result = habilidadeService.alterar(CANDIDATO_ID, HABILIDADE_ID, dto, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(DESCRICAO_ATUALIZADA, result.getDescricao());
        assertEquals(NIVEL, result.getNivel());

        verify(habilidadeRepository, times(1)).save(any(HabilidadeEntity.class));
    }

    // ==================== TESTES DE DELETAR ====================

    @Test
    void testDeletarComSucesso() {
        // Arrange
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.of(entity));
        doNothing().when(habilidadeRepository).delete(entity);

        // Act
        habilidadeService.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt);

        // Assert
        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, times(1)).delete(entity);
    }

    @Test
    void testDeletarHabilidadeNaoEncontrada() {
        // Arrange
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt),
                "Habilidade não encontrada para o ID: " + HABILIDADE_ID
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, never()).delete(any());
    }

    @Test
    void testDeletarHabilidadeNaoPertenceAoCandidato() {
        // Arrange
        Long outroCandidatoId = 999L;
        HabilidadeEntity entity = criarHabilidadeEntity(HABILIDADE_ID, outroCandidatoId, DESCRICAO, NIVEL);
        CandidatoEntity candidatoMock = criarCandidatoEntity(CANDIDATO_ID);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoMock);
        when(habilidadeRepository.findById(HABILIDADE_ID)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt),
                "Habilidade não pertence ao candidato ID: " + CANDIDATO_ID
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, times(1)).findById(HABILIDADE_ID);
        verify(habilidadeRepository, never()).delete(any());
    }

    @Test
    void testDeletarFalhaAutorizacao() {
        // Arrange
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> habilidadeService.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt)
        );

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(habilidadeRepository, never()).findById(anyLong());
    }

    // ==================== TESTES DE BUSCAR POR CANDIDATO ====================

    @Test
    void testBuscarPorCandidatoComSucesso() {
        // Arrange
        List<HabilidadeEntity> entities = Arrays.asList(
                criarHabilidadeEntity(1L, CANDIDATO_ID, "Java", "Avançado"),
                criarHabilidadeEntity(2L, CANDIDATO_ID, "Spring Boot", "Intermediário"),
                criarHabilidadeEntity(3L, CANDIDATO_ID, "SQL", "Avançado")
        );

        when(habilidadeRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(entities);

        // Act
        List<HabilidadeDTO> result = habilidadeService.buscarPorCandidato(CANDIDATO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Java", result.get(0).getDescricao());
        assertEquals("Spring Boot", result.get(1).getDescricao());
        assertEquals("SQL", result.get(2).getDescricao());

        verify(habilidadeRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
    }

    @Test
    void testBuscarPorCandidatoSemHabilidades() {
        // Arrange
        when(habilidadeRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Collections.emptyList());

        // Act
        List<HabilidadeDTO> result = habilidadeService.buscarPorCandidato(CANDIDATO_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(habilidadeRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
    }

    @Test
    void testBuscarPorCandidatoComUmaHabilidade() {
        // Arrange
        List<HabilidadeEntity> entities = Collections.singletonList(
                criarHabilidadeEntity(HABILIDADE_ID, CANDIDATO_ID, DESCRICAO, NIVEL)
        );

        when(habilidadeRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(entities);

        // Act
        List<HabilidadeDTO> result = habilidadeService.buscarPorCandidato(CANDIDATO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DESCRICAO, result.get(0).getDescricao());
        assertEquals(NIVEL, result.get(0).getNivel());

        verify(habilidadeRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
    }

    @Test
    void testBuscarPorCandidatoDiferentesIds() {
        // Arrange
        Long candidato2Id = 2L;
        HabilidadeEntity entity1 = criarHabilidadeEntity(1L, CANDIDATO_ID, "Java", "Avançado");
        HabilidadeEntity entity2 = criarHabilidadeEntity(2L, candidato2Id, "Python", "Básico");

        when(habilidadeRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Collections.singletonList(entity1));
        when(habilidadeRepository.findByIdCandidato(candidato2Id)).thenReturn(Collections.singletonList(entity2));

        // Act
        List<HabilidadeDTO> result1 = habilidadeService.buscarPorCandidato(CANDIDATO_ID);
        List<HabilidadeDTO> result2 = habilidadeService.buscarPorCandidato(candidato2Id);

        // Assert
        assertEquals(1, result1.size());
        assertEquals("Java", result1.get(0).getDescricao());
        assertEquals(1, result2.size());
        assertEquals("Python", result2.get(0).getDescricao());

        verify(habilidadeRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
        verify(habilidadeRepository, times(1)).findByIdCandidato(candidato2Id);
    }

    // ==================== HELPER METHODS ====================

    private HabilidadeDTO criarHabilidadeDTO(Long id, Long idCandidato, String descricao, String nivel) {
        HabilidadeDTO dto = new HabilidadeDTO();
        dto.setId(id);
        dto.setIdCandidato(idCandidato);
        dto.setDescricao(descricao);
        dto.setNivel(nivel);
        return dto;
    }

    private HabilidadeEntity criarHabilidadeEntity(Long id, Long idCandidato, String descricao, String nivel) {
        HabilidadeEntity entity = new HabilidadeEntity();
        entity.setId(id);
        entity.setIdCandidato(idCandidato);
        entity.setDescricao(descricao);
        entity.setNivel(nivel);
        return entity;
    }

    private CandidatoEntity criarCandidatoEntity(Long id) {
        CandidatoEntity candidato = new CandidatoEntity();
        candidato.setId(id);
        candidato.setIdUsuario(1L);
        return candidato;
    }
}