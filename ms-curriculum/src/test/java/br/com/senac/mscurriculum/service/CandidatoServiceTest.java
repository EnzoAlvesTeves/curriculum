package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.AlterarCandidatoRequest;
import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.dto.TipoUsuario;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da Classe CandidatoService")
class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private CandidatoAuthorizationService authorizationService;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private HabilidadeService habilidadeService;

    @Mock
    private ExperienciaService experienciaService;

    @Mock
    private EducacaoService educacaoService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private CandidatoService candidatoService;

    private static final Long USUARIO_ID = 10L;
    private static final Long CANDIDATO_ID = 20L;
    private static final String NOME = "João da Silva";
    private static final String EMAIL = "joao@email.com";
    private static final String SEXO = "MASCULINO";
    private static final String TELEFONE = "11999999999";
    private static final LocalDate DATA_NASCIMENTO = LocalDate.of(1990, 1, 15);
    private static final String RESUMO = "Desenvolvedor Java";
    private static final String NOME_ATUALIZADO = "Maria da Silva";
    private static final String EMAIL_ATUALIZADO = "maria@email.com";
    private static final String SEXO_ATUALIZADO = "FEMININO";
    private static final String TELEFONE_ATUALIZADO = "11888888888";
    private static final LocalDate DATA_NASCIMENTO_ATUALIZADA = LocalDate.of(1992, 3, 10);
    private static final String RESUMO_ATUALIZADO = "Arquiteta de software";

    @BeforeEach
    void setUp() {
        reset(candidatoRepository, authorizationService, enderecoService, habilidadeService, experienciaService, educacaoService);
    }

    @Test
    @DisplayName("Deve cadastrar candidato com sucesso e carregar relacionamentos")
    void testCadastrarComSucesso() {
        CandidatoDTO request = criarCandidatoDTO();
        CandidatoEntity candidatoSalvo = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);
        CandidatoDTO candidatoCompleto = criarCandidatoDTOCompleto();
        UsuarioMeResponse usuario = criarUsuarioMeResponse();

        when(authorizationService.validarTipoCandidato(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.empty());
        when(candidatoRepository.save(any(CandidatoEntity.class))).thenReturn(candidatoSalvo);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidatoSalvo));
        when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getEndereco());
        when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getHabilidades());
        when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getExperiencias());
        when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getEducacoes());

        CandidatoDTO resultado = candidatoService.cadastrar(request, jwt);

        assertNotNull(resultado);
        assertEquals(CANDIDATO_ID, resultado.getId());
        assertEquals(USUARIO_ID, resultado.getIdUsuario());
        assertEquals(NOME, resultado.getNome());
        assertEquals(EMAIL, resultado.getEmail());
        assertEquals(SEXO, resultado.getSexo());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(DATA_NASCIMENTO, resultado.getDataNascimento());
        assertEquals(RESUMO, resultado.getResumoProfissional());
        assertNotNull(resultado.getEndereco());
        assertEquals(2, resultado.getHabilidades().size());
        assertEquals(1, resultado.getExperiencias().size());
        assertEquals(1, resultado.getEducacoes().size());

        ArgumentCaptor<CandidatoEntity> captor = ArgumentCaptor.forClass(CandidatoEntity.class);
        verify(authorizationService, times(1)).validarTipoCandidato(jwt);
        verify(candidatoRepository, times(1)).findByIdUsuario(USUARIO_ID);
        verify(candidatoRepository, times(1)).save(captor.capture());
        assertEquals(USUARIO_ID, captor.getValue().getIdUsuario());
        assertEquals(NOME, captor.getValue().getNome());
        assertEquals(EMAIL, captor.getValue().getEmail());
        verify(enderecoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(habilidadeService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(experienciaService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(educacaoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve cadastrar candidato sem relacionamentos opcionais")
    void testCadastrarSemRelacionamentos() {
        CandidatoDTO request = criarCandidatoDTO();
        request.setEndereco(null);
        request.setHabilidades(null);
        request.setExperiencias(null);
        request.setEducacoes(null);

        CandidatoEntity candidatoSalvo = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);
        UsuarioMeResponse usuario = criarUsuarioMeResponse();

        when(authorizationService.validarTipoCandidato(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.empty());
        when(candidatoRepository.save(any(CandidatoEntity.class))).thenReturn(candidatoSalvo);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidatoSalvo));
        when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarEnderecoDTO());
        when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(List.of());
        when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(List.of());
        when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(List.of());

        CandidatoDTO resultado = candidatoService.cadastrar(request, jwt);

        assertNotNull(resultado);
        verify(enderecoService, never()).cadastrar(anyLong(), any(EnderecoDTO.class));
        verify(habilidadeService, never()).cadastrar(anyLong(), any(HabilidadeDTO.class));
        verify(experienciaService, never()).cadastrar(anyLong(), any(ExperienciaDTO.class));
        verify(educacaoService, never()).cadastrar(anyLong(), any(EducacaoDTO.class));
        verify(enderecoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(habilidadeService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(experienciaService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(educacaoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar conflito ao cadastrar candidato duplicado")
    void testCadastrarDuplicado() {
        CandidatoDTO request = criarCandidatoDTO();
        UsuarioMeResponse usuario = criarUsuarioMeResponse();

        when(authorizationService.validarTipoCandidato(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.of(criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO)));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoService.cadastrar(request, jwt)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Candidato já cadastrado para este usuário", exception.getReason());
        verify(candidatoRepository, never()).save(any());
        verify(enderecoService, never()).cadastrar(anyLong(), any());
        verify(habilidadeService, never()).cadastrar(anyLong(), any());
        verify(experienciaService, never()).cadastrar(anyLong(), any());
        verify(educacaoService, never()).cadastrar(anyLong(), any());
    }

    @Test
    @DisplayName("Deve alterar candidato com sucesso")
    void testAlterarComSucesso() {
        AlterarCandidatoRequest request = criarAlterarRequest();
        CandidatoEntity candidatoExistente = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);
        CandidatoEntity candidatoAlterado = criarCandidatoEntity(NOME_ATUALIZADO, EMAIL_ATUALIZADO, SEXO_ATUALIZADO, TELEFONE_ATUALIZADO, DATA_NASCIMENTO_ATUALIZADA, RESUMO_ATUALIZADO);
        CandidatoDTO candidatoCompleto = criarCandidatoDTOCompleto();

        when(authorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidatoExistente);
        when(candidatoRepository.save(any(CandidatoEntity.class))).thenReturn(candidatoAlterado);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidatoAlterado));
        when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getEndereco());
        when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getHabilidades());
        when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getExperiencias());
        when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(candidatoCompleto.getEducacoes());

        CandidatoDTO resultado = candidatoService.alterar(request, jwt);

        assertNotNull(resultado);
        assertEquals(CANDIDATO_ID, resultado.getId());
        assertEquals(NOME_ATUALIZADO, resultado.getNome());
        assertEquals(EMAIL_ATUALIZADO, resultado.getEmail());
        assertEquals(SEXO_ATUALIZADO, resultado.getSexo());
        assertEquals(TELEFONE_ATUALIZADO, resultado.getTelefone());
        assertEquals(DATA_NASCIMENTO_ATUALIZADA, resultado.getDataNascimento());
        assertEquals(RESUMO_ATUALIZADO, resultado.getResumoProfissional());

        ArgumentCaptor<CandidatoEntity> captor = ArgumentCaptor.forClass(CandidatoEntity.class);
        verify(authorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(candidatoRepository, times(1)).save(captor.capture());
        assertEquals(NOME_ATUALIZADO, captor.getValue().getNome());
        assertEquals(EMAIL_ATUALIZADO, captor.getValue().getEmail());
        verify(enderecoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(habilidadeService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(experienciaService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(educacaoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar not found ao alterar candidato inexistente")
    void testAlterarCandidatoNaoEncontrado() {
        AlterarCandidatoRequest request = criarAlterarRequest();
        when(authorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoService.alterar(request, jwt)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar candidato por ID com sucesso")
    void testBuscarPorIdComSucesso() {
        CandidatoEntity candidato = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidato));
        when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarEnderecoDTO());
        when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarHabilidadesDTO());
        when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarExperienciasDTO());
        when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarEducacoesDTO());

        CandidatoDTO resultado = candidatoService.buscarPorId(CANDIDATO_ID);

        assertNotNull(resultado);
        assertEquals(CANDIDATO_ID, resultado.getId());
        assertEquals(USUARIO_ID, resultado.getIdUsuario());
        assertEquals(NOME, resultado.getNome());
        assertEquals(2, resultado.getHabilidades().size());
        assertEquals(1, resultado.getExperiencias().size());
        assertEquals(1, resultado.getEducacoes().size());
        verify(candidatoRepository, times(1)).findById(CANDIDATO_ID);
        verify(enderecoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(habilidadeService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(experienciaService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(educacaoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar not found ao buscar candidato inexistente")
    void testBuscarPorIdNaoEncontrado() {
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoService.buscarPorId(CANDIDATO_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidato não encontrado", exception.getReason());
        verify(enderecoService, never()).buscarPorCandidato(anyLong());
        verify(habilidadeService, never()).buscarPorCandidato(anyLong());
        verify(experienciaService, never()).buscarPorCandidato(anyLong());
        verify(educacaoService, never()).buscarPorCandidato(anyLong());
    }

    @Test
    @DisplayName("Deve excluir candidato autenticado com sucesso")
    void testDeletarComSucesso() {
        UsuarioMeResponse usuario = criarUsuarioMeResponse();
        CandidatoEntity candidato = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);

        when(authorizationService.usuarioAtual(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.of(candidato));
        when(authorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(candidato);

        candidatoService.deletar(jwt);

        verify(authorizationService, times(1)).usuarioAtual(jwt);
        verify(candidatoRepository, times(1)).findByIdUsuario(USUARIO_ID);
        verify(authorizationService, times(1)).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(candidatoRepository, times(1)).deleteById(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar not found ao excluir candidato inexistente")
    void testDeletarCandidatoNaoEncontrado() {
        UsuarioMeResponse usuario = criarUsuarioMeResponse();

        when(authorizationService.usuarioAtual(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoService.deletar(jwt)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidato não encontrado", exception.getReason());
        verify(candidatoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve buscar candidato autenticado com sucesso")
    void testMeComSucesso() {
        UsuarioMeResponse usuario = criarUsuarioMeResponse();
        CandidatoEntity candidato = criarCandidatoEntity(NOME, EMAIL, SEXO, TELEFONE, DATA_NASCIMENTO, RESUMO);

        when(authorizationService.usuarioAtual(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.of(candidato));
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidato));
        when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarEnderecoDTO());
        when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarHabilidadesDTO());
        when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarExperienciasDTO());
        when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(criarEducacoesDTO());

        CandidatoDTO resultado = candidatoService.me(jwt);

        assertNotNull(resultado);
        assertEquals(CANDIDATO_ID, resultado.getId());
        assertEquals(USUARIO_ID, resultado.getIdUsuario());
        verify(authorizationService, times(1)).usuarioAtual(jwt);
        verify(candidatoRepository, times(1)).findByIdUsuario(USUARIO_ID);
        verify(candidatoRepository, times(1)).findById(CANDIDATO_ID);
        verify(enderecoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(habilidadeService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(experienciaService, times(1)).buscarPorCandidato(CANDIDATO_ID);
        verify(educacaoService, times(1)).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar not found ao buscar candidato autenticado inexistente")
    void testMeCandidatoNaoEncontrado() {
        UsuarioMeResponse usuario = criarUsuarioMeResponse();

        when(authorizationService.usuarioAtual(jwt)).thenReturn(usuario);
        when(candidatoRepository.findByIdUsuario(USUARIO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoService.me(jwt)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidato não encontrado", exception.getReason());
        verify(enderecoService, never()).buscarPorCandidato(anyLong());
        verify(habilidadeService, never()).buscarPorCandidato(anyLong());
        verify(experienciaService, never()).buscarPorCandidato(anyLong());
        verify(educacaoService, never()).buscarPorCandidato(anyLong());
    }

    private CandidatoDTO criarCandidatoDTO() {
        CandidatoDTO dto = new CandidatoDTO();
        dto.setNome(NOME);
        dto.setEmail(EMAIL);
        dto.setSexo(SEXO);
        dto.setTelefone(TELEFONE);
        dto.setDataNascimento(DATA_NASCIMENTO);
        dto.setResumoProfissional(RESUMO);
        dto.setEndereco(criarEnderecoDTO());
        dto.setHabilidades(criarHabilidadesDTO());
        dto.setExperiencias(criarExperienciasDTO());
        dto.setEducacoes(criarEducacoesDTO());
        return dto;
    }

    private CandidatoDTO criarCandidatoDTOCompleto() {
        CandidatoDTO dto = new CandidatoDTO();
        dto.setId(CANDIDATO_ID);
        dto.setIdUsuario(USUARIO_ID);
        dto.setNome(NOME);
        dto.setEmail(EMAIL);
        dto.setSexo(SEXO);
        dto.setTelefone(TELEFONE);
        dto.setDataNascimento(DATA_NASCIMENTO);
        dto.setResumoProfissional(RESUMO);
        dto.setEndereco(criarEnderecoDTO());
        dto.setHabilidades(criarHabilidadesDTO());
        dto.setExperiencias(criarExperienciasDTO());
        dto.setEducacoes(criarEducacoesDTO());
        return dto;
    }

    private AlterarCandidatoRequest criarAlterarRequest() {
        return new AlterarCandidatoRequest(
                CANDIDATO_ID,
                USUARIO_ID,
                NOME_ATUALIZADO,
                EMAIL_ATUALIZADO,
                SEXO_ATUALIZADO,
                TELEFONE_ATUALIZADO,
                DATA_NASCIMENTO_ATUALIZADA,
                RESUMO_ATUALIZADO
        );
    }

    private UsuarioMeResponse criarUsuarioMeResponse() {
        return new UsuarioMeResponse(USUARIO_ID, TipoUsuario.CANDIDATO, "João", "Silva", EMAIL);
    }

    private CandidatoEntity criarCandidatoEntity(String nome, String email, String sexo, String telefone, LocalDate dataNascimento, String resumo) {
        return new CandidatoEntity(CANDIDATO_ID, nome, email, sexo, telefone, dataNascimento, resumo, USUARIO_ID, null, null);
    }

    private EnderecoDTO criarEnderecoDTO() {
        return new EnderecoDTO(
                1L,
                CANDIDATO_ID,
                "Rua A",
                "100",
                "Apto 1",
                "São Paulo",
                "SP",
                "01000-000",
                "Centro",
                new BigDecimal("-23.550520"),
                new BigDecimal("-46.633308")
        );
    }

    private List<HabilidadeDTO> criarHabilidadesDTO() {
        return List.of(
                new HabilidadeDTO(1L, CANDIDATO_ID, "Java", "Avançado"),
                new HabilidadeDTO(2L, CANDIDATO_ID, "Spring Boot", "Intermediário")
        );
    }

    private List<ExperienciaDTO> criarExperienciasDTO() {
        return List.of(
                new ExperienciaDTO(
                        1L,
                        CANDIDATO_ID,
                        "Desenvolvedor Pleno",
                        "Empresa XYZ",
                        "APIs REST",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2023, 12, 31)
                )
        );
    }

    private List<EducacaoDTO> criarEducacoesDTO() {
        return List.of(
                new EducacaoDTO(
                        1L,
                        CANDIDATO_ID,
                        "Análise e Desenvolvimento de Sistemas",
                        "Tecnólogo",
                        "Senac",
                        LocalDate.of(2014, 1, 1),
                        LocalDate.of(2016, 12, 31)
                )
        );
    }
}