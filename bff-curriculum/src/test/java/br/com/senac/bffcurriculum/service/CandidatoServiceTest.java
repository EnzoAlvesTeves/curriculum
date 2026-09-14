package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.CandidatoClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.EnderecoDTO;
import br.com.senac.bffcurriculum.dto.EducacaoDTO;
import br.com.senac.bffcurriculum.dto.ExperienciaDTO;
import br.com.senac.bffcurriculum.dto.HabilidadeDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.enums.Sexo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatoServiceTest {

    @Mock
    private CandidatoClient candidatoClient;

    @InjectMocks
    private CandidatoService candidatoService;

    @Test
    @DisplayName("Deve buscar candidato por usuarioId com sucesso")
    void deveBuscarCandidatoPorUsuarioIdComSucesso() {
        CandidatoDTO expectedResponse = novoCandidatoDTO(1L, 10L);
        when(candidatoClient.getByUsuarioId(10L)).thenReturn(expectedResponse);

        CandidatoDTO actualResponse = candidatoService.getByUsuarioId(10L);

        assertSame(expectedResponse, actualResponse);
        verify(candidatoClient).getByUsuarioId(10L);
    }

    @Test
    @DisplayName("Deve retornar null quando ocorrer erro ao buscar candidato por usuarioId")
    void deveRetornarNullQuandoOcorrerErroAoBuscarCandidatoPorUsuarioId() {
        when(candidatoClient.getByUsuarioId(10L)).thenThrow(new RuntimeException("erro"));

        CandidatoDTO actualResponse = candidatoService.getByUsuarioId(10L);

        assertNull(actualResponse);
        verify(candidatoClient).getByUsuarioId(10L);
    }

    @Test
    @DisplayName("Deve buscar candidato por id com sucesso")
    void deveBuscarCandidatoPorIdComSucesso() {
        CandidatoDTO expectedResponse = novoCandidatoDTO(1L, 10L);
        when(candidatoClient.getById(1L)).thenReturn(expectedResponse);

        CandidatoDTO actualResponse = candidatoService.getById(1L);

        assertSame(expectedResponse, actualResponse);
        verify(candidatoClient).getById(1L);
    }

    @Test
    @DisplayName("Deve listar candidatos com sucesso")
    void deveListarCandidatosComSucesso() {
        List<CandidatoDTO> expectedResponse = List.of(novoCandidatoDTO(1L, 10L), novoCandidatoDTO(2L, 11L));
        when(candidatoClient.getall()).thenReturn(expectedResponse);

        List<CandidatoDTO> actualResponse = candidatoService.getall();

        assertSame(expectedResponse, actualResponse);
        assertEquals(2, actualResponse.size());
        verify(candidatoClient).getall();
    }

    @Test
    @DisplayName("Deve criar candidato delegando ao client")
    void deveCriarCandidatoDelegandoAoClient() {
        CandidatoDTO candidate = novoCandidatoDTO(1L, 10L);
        when(candidatoClient.create(candidate)).thenReturn(candidate);

        CandidatoDTO actualResponse = candidatoService.create(candidate);

        assertSame(candidate, actualResponse);
        verify(candidatoClient).create(candidate);
    }

    @Test
    @DisplayName("Deve atualizar candidato delegando ao client")
    void deveAtualizarCandidatoDelegandoAoClient() {
        CandidatoDTO candidate = novoCandidatoDTO(1L, 10L);
        when(candidatoClient.update(candidate)).thenReturn(candidate);

        CandidatoDTO actualResponse = candidatoService.update(candidate);

        assertSame(candidate, actualResponse);
        verify(candidatoClient).update(candidate);
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

    private UsuarioDTO novoUsuarioDTO(Long id) {
        return new UsuarioDTO(id, "Maria", "maria@email.com", "123456");
    }
}
