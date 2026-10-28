package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da Classe EducacaoService")
class EducacaoServiceTest {

    @Mock
    private EducacaoRepository educacaoRepository;

    @Mock
    private CandidatoAuthorizationService candidatoAuthorizationService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private EducacaoService educacaoService;

    private static final Long CANDIDATO_ID = 1L;
    private static final Long OUTRO_CANDIDATO_ID = 2L;
    private static final Long EDUCACAO_ID = 10L;
    private static final String CURSO = "Análise e Desenvolvimento de Sistemas";
    private static final String GRAU = "Tecnólogo";
    private static final String INSTITUICAO = "Senac";
    private static final LocalDate DATA_INICIO = LocalDate.of(2014, 1, 1);
    private static final LocalDate DATA_FIM = LocalDate.of(2016, 12, 31);
    private static final String CURSO_ATUALIZADO = "Engenharia de Software";
    private static final String GRAU_ATUALIZADO = "Bacharelado";
    private static final String INSTITUICAO_ATUALIZADA = "Universidade X";
    private static final LocalDate DATA_INICIO_ATUALIZADA = LocalDate.of(2017, 2, 1);
    private static final LocalDate DATA_FIM_ATUALIZADA = LocalDate.of(2021, 12, 31);

    @BeforeEach
    void setUp() {
        reset(educacaoRepository, candidatoAuthorizationService);
    }

    @Test
    @DisplayName("Deve cadastrar educação com sucesso usando JWT")
    void testCadastrarComJwt() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);
        EducacaoEntity entitySalva = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);
        ArgumentCaptor<EducacaoEntity> captor = ArgumentCaptor.forClass(EducacaoEntity.class);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.save(any(EducacaoEntity.class))).thenReturn(entitySalva);

        EducacaoDTO resultado = educacaoService.cadastrar(CANDIDATO_ID, dto, jwt);

        assertNotNull(resultado);
        assertEquals(EDUCACAO_ID, resultado.getId());
        assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        assertEquals(CURSO, resultado.getCurso());
        assertEquals(GRAU, resultado.getGrau());
        assertEquals(INSTITUICAO, resultado.getInstituicao());
        assertEquals(DATA_INICIO, resultado.getDataInicio());
        assertEquals(DATA_FIM, resultado.getDataFim());

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).save(captor.capture());
        assertEquals(CANDIDATO_ID, captor.getValue().getIdCandidato());
        assertEquals(CURSO, captor.getValue().getCurso());
        assertEquals(GRAU, captor.getValue().getGrau());
        assertEquals(INSTITUICAO, captor.getValue().getInstituicao());
    }

    @Test
    @DisplayName("Deve cadastrar educação sem JWT")
    void testCadastrarSemJwt() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);
        EducacaoEntity entitySalva = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);

        when(educacaoRepository.save(any(EducacaoEntity.class))).thenReturn(entitySalva);

        EducacaoDTO resultado = educacaoService.cadastrar(CANDIDATO_ID, dto);

        assertNotNull(resultado);
        assertEquals(EDUCACAO_ID, resultado.getId());
        assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        assertEquals(CURSO, resultado.getCurso());
        verify(educacaoRepository, times(1)).save(any(EducacaoEntity.class));
        verify(candidatoAuthorizationService, never()).validarAcessoDoUsuario(anyLong(), any(Jwt.class));
    }

    @Test
    @DisplayName("Deve falhar no cadastro quando a autorização falhar")
    void testCadastrarFalhaAutorizacao() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        assertThrows(RuntimeException.class, () -> educacaoService.cadastrar(CANDIDATO_ID, dto, jwt));

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve alterar educação com sucesso")
    void testAlterarComSucesso() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);
        EducacaoEntity entityExistente = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);
        EducacaoEntity entityAtualizada = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);
        ArgumentCaptor<EducacaoEntity> captor = ArgumentCaptor.forClass(EducacaoEntity.class);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.of(entityExistente));
        when(educacaoRepository.save(any(EducacaoEntity.class))).thenReturn(entityAtualizada);

        EducacaoDTO resultado = educacaoService.alterar(CANDIDATO_ID, EDUCACAO_ID, dto, jwt);

        assertNotNull(resultado);
        assertEquals(EDUCACAO_ID, resultado.getId());
        assertEquals(CURSO_ATUALIZADO, resultado.getCurso());
        assertEquals(GRAU_ATUALIZADO, resultado.getGrau());
        assertEquals(INSTITUICAO_ATUALIZADA, resultado.getInstituicao());
        assertEquals(DATA_INICIO_ATUALIZADA, resultado.getDataInicio());
        assertEquals(DATA_FIM_ATUALIZADA, resultado.getDataFim());

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, times(1)).save(captor.capture());
        assertEquals(CURSO_ATUALIZADO, captor.getValue().getCurso());
        assertEquals(GRAU_ATUALIZADO, captor.getValue().getGrau());
        assertEquals(INSTITUICAO_ATUALIZADA, captor.getValue().getInstituicao());
        assertEquals(DATA_INICIO_ATUALIZADA, captor.getValue().getDataInicio());
        assertEquals(DATA_FIM_ATUALIZADA, captor.getValue().getDataFim());
    }

    @Test
    @DisplayName("Deve falhar ao alterar educação inexistente")
    void testAlterarNaoEncontrada() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> educacaoService.alterar(CANDIDATO_ID, EDUCACAO_ID, dto, jwt)
        );

        assertEquals("Educação não encontrada para o ID: " + EDUCACAO_ID, exception.getMessage());
        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao alterar educação de outro candidato")
    void testAlterarNaoPertenceAoCandidato() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);
        EducacaoEntity entityExistente = criarEducacaoEntity(EDUCACAO_ID, OUTRO_CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.of(entityExistente));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> educacaoService.alterar(CANDIDATO_ID, EDUCACAO_ID, dto, jwt)
        );

        assertEquals("Educação não pertence ao candidato ID: " + CANDIDATO_ID, exception.getMessage());
        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar na alteração quando a autorização falhar")
    void testAlterarFalhaAutorizacao() {
        EducacaoDTO dto = criarEducacaoDTO(CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        assertThrows(RuntimeException.class, () -> educacaoService.alterar(CANDIDATO_ID, EDUCACAO_ID, dto, jwt));

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, never()).findById(anyLong());
        verify(educacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar educação com sucesso")
    void testDeletarComSucesso() {
        EducacaoEntity entityExistente = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.of(entityExistente));
        doNothing().when(educacaoRepository).delete(entityExistente);

        educacaoService.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt);

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, times(1)).delete(entityExistente);
    }

    @Test
    @DisplayName("Deve falhar ao deletar educação inexistente")
    void testDeletarNaoEncontrada() {
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> educacaoService.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt)
        );

        assertEquals("Educação não encontrada para o ID: " + EDUCACAO_ID, exception.getMessage());
        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, never()).delete(any(EducacaoEntity.class));
    }

    @Test
    @DisplayName("Deve falhar ao deletar educação de outro candidato")
    void testDeletarNaoPertenceAoCandidato() {
        EducacaoEntity entityExistente = criarEducacaoEntity(EDUCACAO_ID, OUTRO_CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(educacaoRepository.findById(EDUCACAO_ID)).thenReturn(Optional.of(entityExistente));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> educacaoService.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt)
        );

        assertEquals("Educação não pertence ao candidato ID: " + CANDIDATO_ID, exception.getMessage());
        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, times(1)).findById(EDUCACAO_ID);
        verify(educacaoRepository, never()).delete(any(EducacaoEntity.class));
    }

    @Test
    @DisplayName("Deve falhar na exclusão quando a autorização falhar")
    void testDeletarFalhaAutorizacao() {
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt))
                .thenThrow(new RuntimeException("Acesso negado"));

        assertThrows(RuntimeException.class, () -> educacaoService.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt));

        verify(candidatoAuthorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(educacaoRepository, never()).findById(anyLong());
        verify(educacaoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve buscar educações por candidato")
    void testBuscarPorCandidato() {
        EducacaoEntity entity1 = criarEducacaoEntity(EDUCACAO_ID, CANDIDATO_ID, CURSO, GRAU, INSTITUICAO, DATA_INICIO, DATA_FIM);
        EducacaoEntity entity2 = criarEducacaoEntity(EDUCACAO_ID + 1, CANDIDATO_ID, CURSO_ATUALIZADO, GRAU_ATUALIZADO, INSTITUICAO_ATUALIZADA, DATA_INICIO_ATUALIZADA, DATA_FIM_ATUALIZADA);

        when(educacaoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(List.of(entity1, entity2));

        List<EducacaoDTO> resultado = educacaoService.buscarPorCandidato(CANDIDATO_ID);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(CURSO, resultado.get(0).getCurso());
        assertEquals(CURSO_ATUALIZADO, resultado.get(1).getCurso());
        verify(educacaoRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o candidato não possui educações")
    void testBuscarPorCandidatoSemEducacoes() {
        when(educacaoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(List.of());

        List<EducacaoDTO> resultado = educacaoService.buscarPorCandidato(CANDIDATO_ID);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(educacaoRepository, times(1)).findByIdCandidato(CANDIDATO_ID);
    }

    private EducacaoDTO criarEducacaoDTO(String curso, String grau, String instituicao, LocalDate dataInicio, LocalDate dataFim) {
        return new EducacaoDTO(null, null, curso, grau, instituicao, dataInicio, dataFim);
    }

    private EducacaoEntity criarEducacaoEntity(Long id, Long idCandidato, String curso, String grau, String instituicao, LocalDate dataInicio, LocalDate dataFim) {
        return new EducacaoEntity(id, idCandidato, curso, grau, instituicao, dataInicio, dataFim);
    }
}
