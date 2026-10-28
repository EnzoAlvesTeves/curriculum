package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VagaControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private VagaService vagaService;

    @Mock
    private Model model;

    @InjectMocks
    private VagaController vagaController;

    @Test
    @DisplayName("Deve cadastrar vaga e redirecionar para lista")
    void deveCadastrarVagaERedirecionarParaLista() {
        VagaDTO vagaDTO = novoVagaDTO(1L);

        String viewName = vagaController.cadastrar(vagaDTO);

        assertEquals("redirect:/vaga/lista/usuario/1", viewName);
        verify(vagaService).cadastrar(vagaDTO);
    }

    @Test
    @DisplayName("Deve abrir formulario de cadastro de vaga com usuario")
    void deveAbrirFormularioDeCadastroDeVagaComUsuario() {
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);
        when(usuarioService.getById(1L)).thenReturn(usuarioDTO);

        String viewName = vagaController.cadastrarVaga(1L, model);

        assertEquals("vaga/cadastro", viewName);
        verify(usuarioService).getById(1L);
        verify(model).addAttribute("usuario", usuarioDTO);
    }

    @Test
    @DisplayName("Deve listar vagas sem mensagem de erro")
    void deveListarVagasSemMensagemDeErro() {
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 1L);
        List<VagaDTO> vagas = List.of(novoVagaDTO(1L), novoVagaDTO(2L));

        when(usuarioService.getById(1L)).thenReturn(usuarioDTO);
        when(candidatoService.getByUsuarioId(1L)).thenReturn(candidatoDTO);
        when(vagaService.getAll()).thenReturn(vagas);

        String viewName = vagaController.listaVaga(1L, null, model);

        assertEquals("vaga/lista", viewName);
        verify(usuarioService).getById(1L);
        verify(candidatoService).getByUsuarioId(1L);
        verify(vagaService).getAll();
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("candidato", candidatoDTO);
        verify(model).addAttribute("vagas", vagas);
        verifyNoMoreInteractions(model);
    }

    @Test
    @DisplayName("Deve listar vagas com mensagem de erro")
    void deveListarVagasComMensagemDeErro() {
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 1L);
        List<VagaDTO> vagas = List.of(novoVagaDTO(1L), novoVagaDTO(2L));
        String error = "Erro ao candidatar-se à vaga";

        when(usuarioService.getById(1L)).thenReturn(usuarioDTO);
        when(candidatoService.getByUsuarioId(1L)).thenReturn(candidatoDTO);
        when(vagaService.getAll()).thenReturn(vagas);

        String viewName = vagaController.listaVaga(1L, error, model);

        assertEquals("vaga/lista", viewName);
        verify(usuarioService).getById(1L);
        verify(candidatoService).getByUsuarioId(1L);
        verify(vagaService).getAll();
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("candidato", candidatoDTO);
        verify(model).addAttribute("vagas", vagas);
        verify(model).addAttribute("error", error);
    }

    private UsuarioDTO novoUsuarioDTO(Long id) {
        return new UsuarioDTO(id, "Maria", "maria@email.com", "123456");
    }

    private CandidatoDTO novoCandidatoDTO(Long id, Long usuarioId) {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setId(id);
        candidatoDTO.setUsuarioId(usuarioId);
        candidatoDTO.setNome("Maria");
        candidatoDTO.setEmail("maria@email.com");
        candidatoDTO.setUsuario(novoUsuarioDTO(usuarioId));
        return candidatoDTO;
    }

    private VagaDTO novoVagaDTO(Long id) {
        return new VagaDTO(id, "Dev Java", "Backend", "Empresa X", "VR, VA", 7500.0);
    }
}
