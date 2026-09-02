
package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.mapper.ExperienciaMapper;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da Classe ExperienciaService")
class ExperienciaServiceTest {

    @Mock
    private ExperienciaRepository experienciaRepository;

    @Mock
    private CandidatoAuthorizationService candidatoAuthorizationService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private ExperienciaService experienciaService;

    private ExperienciaDTO experienciaDTO;
    private ExperienciaEntity experienciaEntity;
    private Long candidatoId;
    private Long experienciaId;

    @BeforeEach
    void setUp() {
        candidatoId = 1L;
        experienciaId = 1L;

        experienciaDTO = new ExperienciaDTO(
                experienciaId,
                candidatoId,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "Desenvolvimento de APIs REST em Java",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2023, 12, 31)
        );

        experienciaEntity = new ExperienciaEntity();
        experienciaEntity.setId(experienciaId);
        experienciaEntity.setIdCandidato(candidatoId);
        experienciaEntity.setCargo(experienciaDTO.getCargo());
        experienciaEntity.setEmpresa(experienciaDTO.getEmpresa());
        experienciaEntity.setResumo(experienciaDTO.getResumo());
        experienciaEntity.setDataInicio(experienciaDTO.getDataInicio());
        experienciaEntity.setDataFim(experienciaDTO.getDataFim());
    }

    @Test
    @DisplayName("Deve cadastrar experiência com sucesso usando JWT")
    void testCadastrarComJwt() {
        // Arrange
        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.save(any(ExperienciaEntity.class))).thenReturn(experienciaEntity);

        // Act
        ExperienciaDTO resultado = experienciaService.cadastrar(candidatoId, experienciaDTO, jwt);

        // Assert
        assertNotNull(resultado);
        assertEquals(experienciaDTO.getCargo(), resultado.getCargo());
        assertEquals(experienciaDTO.getEmpresa(), resultado.getEmpresa());
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).save(any(ExperienciaEntity.class));
    }

    @Test
    @DisplayName("Deve cadastrar experiência sem JWT")
    void testCadastrarSemJwt() {
        // Arrange
        when(experienciaRepository.save(any(ExperienciaEntity.class))).thenReturn(experienciaEntity);

        // Act
        ExperienciaDTO resultado = experienciaService.cadastrar(candidatoId, experienciaDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(experienciaDTO.getCargo(), resultado.getCargo());
        assertEquals(experienciaDTO.getEmpresa(), resultado.getEmpresa());
        verify(experienciaRepository).save(any(ExperienciaEntity.class));
        verify(candidatoAuthorizationService, never()).validarAcessoDoUsuario(anyLong(), any(Jwt.class));
    }

    @Test
    @DisplayName("Deve alterar experiência com sucesso")
    void testAlterarComSucesso() {
        // Arrange
        ExperienciaDTO experienciaAtualizada = new ExperienciaDTO(
                experienciaId,
                candidatoId,
                "Desenvolvedor Senior",
                "Empresa ABC",
                "Desenvolvimento e liderança técnica",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.of(experienciaEntity));
        when(experienciaRepository.save(any(ExperienciaEntity.class))).thenReturn(experienciaEntity);

        // Act
        ExperienciaDTO resultado = experienciaService.alterar(candidatoId, experienciaId, experienciaAtualizada, jwt);

        // Assert
        assertNotNull(resultado);
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
        verify(experienciaRepository).save(any(ExperienciaEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar experiência não encontrada")
    void testAlterarExperienciaNaoEncontrada() {
        // Arrange
        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaService.alterar(candidatoId, experienciaId, experienciaDTO, jwt);
        });

        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar experiência de outro candidato")
    void testAlterarExperienciaDeOutroCandidato() {
        // Arrange
        Long outroCandidatoId = 2L;
        ExperienciaEntity experienciaOutroCandidato = new ExperienciaEntity();
        experienciaOutroCandidato.setId(experienciaId);
        experienciaOutroCandidato.setIdCandidato(outroCandidatoId);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.of(experienciaOutroCandidato));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.alterar(candidatoId, experienciaId, experienciaDTO, jwt);
        });

        assertTrue(exception.getMessage().contains("não pertence ao candidato"));
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
    }

    @Test
    @DisplayName("Deve deletar experiência com sucesso")
    void testDeletarComSucesso() {
        // Arrange
        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.of(experienciaEntity));
        doNothing().when(experienciaRepository).delete(experienciaEntity);

        // Act
        experienciaService.deletar(candidatoId, experienciaId, jwt);

        // Assert
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
        verify(experienciaRepository).delete(experienciaEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar experiência não encontrada")
    void testDeletarExperienciaNaoEncontrada() {
        // Arrange
        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            experienciaService.deletar(candidatoId, experienciaId, jwt);
        });

        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
        verify(experienciaRepository, never()).delete(any(ExperienciaEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar experiência de outro candidato")
    void testDeletarExperienciaDeOutroCandidato() {
        // Arrange
        Long outroCandidatoId = 2L;
        ExperienciaEntity experienciaOutroCandidato = new ExperienciaEntity();
        experienciaOutroCandidato.setId(experienciaId);
        experienciaOutroCandidato.setIdCandidato(outroCandidatoId);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt)).thenReturn(null);
        when(experienciaRepository.findById(experienciaId)).thenReturn(Optional.of(experienciaOutroCandidato));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            experienciaService.deletar(candidatoId, experienciaId, jwt);
        });

        assertTrue(exception.getMessage().contains("não pertence ao candidato"));
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(candidatoId, jwt);
        verify(experienciaRepository).findById(experienciaId);
        verify(experienciaRepository, never()).delete(any(ExperienciaEntity.class));
    }

    @Test
    @DisplayName("Deve buscar experiências por candidato")
    void testBuscarPorCandidato() {
        // Arrange
        ExperienciaEntity experiencia2 = new ExperienciaEntity();
        experiencia2.setId(2L);
        experiencia2.setIdCandidato(candidatoId);
        experiencia2.setCargo("Desenvolvedor Junior");
        experiencia2.setEmpresa("Empresa DEF");

        List<ExperienciaEntity> experiencias = Arrays.asList(experienciaEntity, experiencia2);
        when(experienciaRepository.findByIdCandidato(candidatoId)).thenReturn(experiencias);

        // Act
        List<ExperienciaDTO> resultado = experienciaService.buscarPorCandidato(candidatoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(experienciaRepository).findByIdCandidato(candidatoId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando candidato não possui experiências")
    void testBuscarPorCandidatoSemExperiencias() {
        // Arrange
        when(experienciaRepository.findByIdCandidato(candidatoId)).thenReturn(List.of());

        // Act
        List<ExperienciaDTO> resultado = experienciaService.buscarPorCandidato(candidatoId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(experienciaRepository).findByIdCandidato(candidatoId);
    }
}