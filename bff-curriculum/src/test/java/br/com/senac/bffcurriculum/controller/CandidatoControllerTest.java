package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.EducacaoDTO;
import br.com.senac.bffcurriculum.dto.EnderecoDTO;
import br.com.senac.bffcurriculum.dto.ExperienciaDTO;
import br.com.senac.bffcurriculum.dto.HabilidadeDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.enums.Sexo;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatoControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private Model model;

    @InjectMocks
    private CandidatoController candidatoController;

    @Test
    @DisplayName("Deve buscar candidato e adicionar atributos no model")
    void deveBuscarCandidatoEAdicionarAtributosNoModel() {
        CandidatoDTO candidatoDTO = novoCandidatoDTO(1L, 10L);
        UsuarioDTO usuarioDTO = novoUsuarioDTO(10L);

        when(candidatoService.getById(1L)).thenReturn(candidatoDTO);
        when(usuarioService.getById(10L)).thenReturn(usuarioDTO);

        String viewName = candidatoController.buscar(1L, model);

        assertEquals("candidato/curriculo", viewName);
        assertSame(usuarioDTO, candidatoDTO.getUsuario());
        verify(candidatoService).getById(1L);
        verify(usuarioService).getById(10L);
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("candidato", candidatoDTO);
    }

    @Test
    @DisplayName("Deve carregar formulario de novo candidato com estruturas vazias")
    void deveCarregarFormularioDeNovoCandidatoComEstruturasVazias() {
        UsuarioDTO usuarioDTO = novoUsuarioDTO(10L);
        when(usuarioService.getById(10L)).thenReturn(usuarioDTO);

        String viewName = candidatoController.novoCandidato(10L, model);

        assertEquals("candidato/cadastro", viewName);

        ArgumentCaptor<CandidatoDTO> captor = ArgumentCaptor.forClass(CandidatoDTO.class);
        verify(model).addAttribute(eq("candidato"), captor.capture());
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(usuarioService).getById(10L);

        CandidatoDTO candidatoDTO = captor.getValue();
        assertEquals(10L, candidatoDTO.getUsuarioId());
        assertSame(usuarioDTO, candidatoDTO.getUsuario());
        assertEquals("Maria", candidatoDTO.getNome());
        assertEquals("maria@email.com", candidatoDTO.getEmail());
        assertEquals(1, candidatoDTO.getEducacoes().size());
        assertEquals(1, candidatoDTO.getExperiencias().size());
        assertEquals(1, candidatoDTO.getHabilidades().size());
    }

    @Test
    @DisplayName("Deve listar candidatos no model")
    void deveListarCandidatosNoModel() {
        List<CandidatoDTO> candidatos = List.of(
                novoCandidatoDTO(1L, 10L),
                novoCandidatoDTO(2L, 11L)
        );
        when(candidatoService.getall()).thenReturn(candidatos);

        String viewName = candidatoController.listaCandidatos(model);

        assertEquals("candidato/lista", viewName);
        verify(candidatoService).getall();
        verify(model).addAttribute("candidatos", candidatos);
    }

    @Test
    @DisplayName("Deve cadastrar candidato e redirecionar para pagina do curriculo")
    void deveCadastrarCandidatoERedirecionarParaPaginaDoCurriculo() {
        CandidatoDTO candidatoDTO = novoCandidatoDTO(0L, 10L);
        CandidatoDTO candidatoSalvo = novoCandidatoDTO(5L, 10L);
        when(candidatoService.create(candidatoDTO)).thenReturn(candidatoSalvo);

        String viewName = candidatoController.cadastrar(candidatoDTO, model);

        assertEquals("redirect:/candidato/5", viewName);
        verify(candidatoService).create(candidatoDTO);
    }

    @Test
    @DisplayName("Deve editar candidato e redirecionar para pagina do curriculo")
    void deveEditarCandidatoERedirecionarParaPaginaDoCurriculo() {
        CandidatoDTO candidatoDTO = novoCandidatoDTO(5L, 10L);
        CandidatoDTO candidatoAtualizado = novoCandidatoDTO(5L, 10L);
        when(candidatoService.update(candidatoDTO)).thenReturn(candidatoAtualizado);

        String viewName = candidatoController.editar(candidatoDTO, model);

        assertEquals("redirect:/candidato/5", viewName);
        verify(candidatoService).update(candidatoDTO);
    }

    private CandidatoDTO novoCandidatoDTO(Long id, Long usuarioId) {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setId(id);
        candidatoDTO.setNome("Maria");
        candidatoDTO.setEmail("maria@email.com");
        candidatoDTO.setTelefone("11999999999");
        candidatoDTO.setSexo(Sexo.FEMININO);
        candidatoDTO.setDataNascimento(LocalDate.of(1995, 1, 1));
        candidatoDTO.setResumoProfissional("Resumo");
        candidatoDTO.setUsuarioId(usuarioId);
        candidatoDTO.setEndereco(novoEnderecoDTO());
        candidatoDTO.setEducacoes(List.of(novaEducacaoDTO(usuarioId)));
        candidatoDTO.setExperiencias(List.of(novaExperienciaDTO(usuarioId)));
        candidatoDTO.setHabilidades(List.of(novaHabilidadeDTO(usuarioId)));
        candidatoDTO.setUsuario(novoUsuarioDTO(usuarioId));
        return candidatoDTO;
    }

    private UsuarioDTO novoUsuarioDTO(Long id) {
        return new UsuarioDTO(id, "Maria", "maria@email.com", "123456");
    }

    private EnderecoDTO novoEnderecoDTO() {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(1L);
        enderecoDTO.setRua("Rua A");
        enderecoDTO.setNumero("100");
        enderecoDTO.setComplemento("Ap 1");
        enderecoDTO.setCidade("São Paulo");
        enderecoDTO.setEstado("SP");
        enderecoDTO.setCep("01001000");
        enderecoDTO.setBairro("Centro");
        return enderecoDTO;
    }

    private EducacaoDTO novaEducacaoDTO(Long candidatoId) {
        return new EducacaoDTO(1L, "Superior", LocalDate.of(2015, 1, 1), LocalDate.of(2019, 12, 1), "Faculdade X", "ADS", candidatoId);
    }

    private ExperienciaDTO novaExperienciaDTO(Long candidatoId) {
        return new ExperienciaDTO(1L, "Dev", "Empresa X", LocalDate.of(2020, 1, 1), LocalDate.of(2024, 1, 1), candidatoId);
    }

    private HabilidadeDTO novaHabilidadeDTO(Long candidatoId) {
        return new HabilidadeDTO(1L, "Java", "Avançado", "Backend", candidatoId);
    }
}
