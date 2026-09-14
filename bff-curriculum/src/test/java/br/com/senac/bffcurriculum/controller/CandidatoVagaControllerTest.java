package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.controller.request.Candidatura;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.CandidatoVagaService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import br.com.senac.bffcurriculum.service.VagaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatoVagaControllerTest {

    private static final String MENSAGEM_ERRO = "Erro ao candidatar-se à vaga: Candidato já inscrito nesta vaga.";

    @Mock
    private CandidatoVagaService candidatoVagaService;

    @Mock
    private VagaService vagaService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Model model;

    @InjectMocks
    private CandidatoVagaController candidatoVagaController;

    @Test
    @DisplayName("Deve candidatar usuario a vaga com sucesso")
    void deveCandidatarUsuarioAVagaComSucesso() {
        Candidatura candidatura = novaCandidatura();
        VagaDTO vagaDTO = novaVagaDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 100L);
        UsuarioDTO usuarioDTO = novoUsuarioDTO(100L);

        when(vagaService.getById(1L)).thenReturn(vagaDTO);
        when(candidatoService.getById(10L)).thenReturn(candidatoDTO);
        when(usuarioService.getById(100L)).thenReturn(usuarioDTO);

        String viewName = candidatoVagaController.candidatarVaga(candidatura, model);

        assertEquals("redirect:/candidato-vaga/candidato/10", viewName);
        verify(vagaService).getById(1L);
        verify(candidatoService).getById(10L);
        verify(usuarioService).getById(100L);
        verify(candidatoVagaService).candidatarVaga(vagaDTO, candidatoDTO);
    }

    @Test
    @DisplayName("Deve redirecionar com mensagem de erro quando candidatura falhar")
    void deveRedirecionarComMensagemDeErroQuandoCandidaturaFalhar() {
        Candidatura candidatura = novaCandidatura();
        VagaDTO vagaDTO = novaVagaDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 100L);
        UsuarioDTO usuarioDTO = novoUsuarioDTO(100L);

        when(vagaService.getById(1L)).thenReturn(vagaDTO);
        when(candidatoService.getById(10L)).thenReturn(candidatoDTO);
        when(usuarioService.getById(100L)).thenReturn(usuarioDTO);
        org.mockito.Mockito.doThrow(new RuntimeException("erro"))
                .when(candidatoVagaService).candidatarVaga(vagaDTO, candidatoDTO);

        String viewName = candidatoVagaController.candidatarVaga(candidatura, model);

        assertEquals("redirect:/vaga/lista/usuario/100?error=" + MENSAGEM_ERRO, viewName);
        verify(model).addAttribute("errorMessage", MENSAGEM_ERRO);
        verify(candidatoVagaService).candidatarVaga(vagaDTO, candidatoDTO);
    }

    @Test
    @DisplayName("Deve listar candidatos por vaga com sucesso")
    void deveListarCandidatosPorVagaComSucesso() {
        List<CandidatoVagaDTO> candidaturas = List.of(
                novaCandidaturaVagaDTO(1L, 10L, 1L),
                novaCandidaturaVagaDTO(2L, 11L, 1L)
        );
        CandidatoDTO candidato1 = novoCandidatoDTO(10L, 100L);
        CandidatoDTO candidato2 = novoCandidatoDTO(11L, 101L);
        UsuarioDTO usuario1 = novoUsuarioDTO(100L);
        UsuarioDTO usuario2 = novoUsuarioDTO(101L);

        when(candidatoVagaService.candidatosPorVaga(1L)).thenReturn(candidaturas);
        when(candidatoService.getById(10L)).thenReturn(candidato1);
        when(candidatoService.getById(11L)).thenReturn(candidato2);
        when(usuarioService.getById(100L)).thenReturn(usuario1);
        when(usuarioService.getById(101L)).thenReturn(usuario2);

        String viewName = candidatoVagaController.candidatosPorVaga(1L, 100L, model);

        assertEquals("vaga/candidatos", viewName);
        verify(model).addAttribute("usuario", usuario1);
        verify(model).addAttribute("candidatos", List.of(candidato1, candidato2));
        assertSame(usuario1, candidato1.getUsuario());
        assertSame(usuario2, candidato2.getUsuario());
    }

    @Test
    @DisplayName("Deve listar vagas por candidato com sucesso")
    void deveListarVagasPorCandidatoComSucesso() {
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 100L);
        UsuarioDTO usuarioDTO = novoUsuarioDTO(100L);
        List<CandidatoVagaDTO> candidaturas = List.of(
                novaCandidaturaVagaDTO(1L, 10L, 1L),
                novaCandidaturaVagaDTO(2L, 10L, 2L)
        );
        VagaDTO vaga1 = novaVagaDTO(1L);
        VagaDTO vaga2 = novaVagaDTO(2L);

        when(candidatoService.getById(10L)).thenReturn(candidatoDTO);
        when(usuarioService.getById(100L)).thenReturn(usuarioDTO);
        when(candidatoVagaService.vagasPorCandidato(10L)).thenReturn(candidaturas);
        when(vagaService.getById(1L)).thenReturn(vaga1);
        when(vagaService.getById(2L)).thenReturn(vaga2);

        String viewName = candidatoVagaController.vagasPorCandidato(10L, model);

        assertEquals("candidato/vagas", viewName);
        assertSame(usuarioDTO, candidatoDTO.getUsuario());
        verify(model).addAttribute("candidato", candidatoDTO);
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("vagas", List.of(vaga1, vaga2));
    }

    @Test
    @DisplayName("Deve remover candidatura e redirecionar")
    void deveRemoverCandidaturaERedirecionar() {
        String viewName = candidatoVagaController.deletarCandidatura(99L);

        assertEquals("redirect:/candidato/vagas", viewName);
        verify(candidatoVagaService).removerCandidatura(99L);
    }

    private Candidatura novaCandidatura() {
        return new Candidatura(10L, 1L);
    }

    private UsuarioDTO novoUsuarioDTO(Long id) {
        return new UsuarioDTO(id, "Maria", "maria@email.com", "123456");
    }

    private CandidatoDTO novoCandidatoDTO(Long id, Long usuarioId) {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setId(id);
        candidatoDTO.setNome("Maria");
        candidatoDTO.setEmail("maria@email.com");
        candidatoDTO.setUsuarioId(usuarioId);
        candidatoDTO.setUsuario(novoUsuarioDTO(usuarioId));
        return candidatoDTO;
    }

    private VagaDTO novaVagaDTO(Long id) {
        return new VagaDTO(id, "Dev Java", "Backend", "Empresa X", "VR, VA", 7500.0);
    }

    private CandidatoVagaDTO novaCandidaturaVagaDTO(Long id, Long candidatoId, Long vagaId) {
        CandidatoVagaDTO dto = new CandidatoVagaDTO();
        dto.setId(id);
        dto.setCandidatoId(candidatoId);
        dto.setVaga(novaVagaDTO(vagaId));
        return dto;
    }
}
