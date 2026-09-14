package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.CandidatoVagaClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidatoVagaServiceTest {

    @Mock
    private CandidatoVagaClient candidatoVagaClient;

    @InjectMocks
    private CandidatoVagaService candidatoVagaService;

    @Test
    @DisplayName("Deve candidatar usuario a vaga montando o DTO corretamente")
    void deveCandidatarUsuarioAVagaMontandoODtoCorretamente() {
        VagaDTO vagaDTO = novaVagaDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L);

        candidatoVagaService.candidatarVaga(vagaDTO, candidatoDTO);

        ArgumentCaptor<CandidatoVagaDTO> captor = ArgumentCaptor.forClass(CandidatoVagaDTO.class);
        verify(candidatoVagaClient).create(captor.capture());

        CandidatoVagaDTO actualRequest = captor.getValue();
        assertEquals(candidatoDTO.getId(), actualRequest.getCandidatoId());
        assertSame(vagaDTO, actualRequest.getVaga());
        assertNotNull(actualRequest.getDataInscricao());
    }

    @Test
    @DisplayName("Deve listar candidaturas por vaga delegando ao client")
    void deveListarCandidaturasPorVagaDelegandoAoClient() {
        List<CandidatoVagaDTO> expectedResponse = List.of(novaCandidaturaDTO(1L, 10L, 1L), novaCandidaturaDTO(2L, 11L, 1L));
        when(candidatoVagaClient.getByVagaId(1L)).thenReturn(expectedResponse);

        List<CandidatoVagaDTO> actualResponse = candidatoVagaService.candidatosPorVaga(1L);

        assertSame(expectedResponse, actualResponse);
        assertEquals(2, actualResponse.size());
        verify(candidatoVagaClient).getByVagaId(1L);
    }

    @Test
    @DisplayName("Deve listar vagas por candidato delegando ao client")
    void deveListarVagasPorCandidatoDelegandoAoClient() {
        List<CandidatoVagaDTO> expectedResponse = List.of(novaCandidaturaDTO(1L, 10L, 1L), novaCandidaturaDTO(2L, 10L, 2L));
        when(candidatoVagaClient.getByCandidatoId(10L)).thenReturn(expectedResponse);

        List<CandidatoVagaDTO> actualResponse = candidatoVagaService.vagasPorCandidato(10L);

        assertSame(expectedResponse, actualResponse);
        assertEquals(2, actualResponse.size());
        verify(candidatoVagaClient).getByCandidatoId(10L);
    }

    @Test
    @DisplayName("Deve remover candidatura delegando ao client")
    void deveRemoverCandidaturaDelegandoAoClient() {
        candidatoVagaService.removerCandidatura(99L);

        verify(candidatoVagaClient).delete(99L);
    }

    private VagaDTO novaVagaDTO(Long id) {
        return new VagaDTO(id, "Desenvolvedor Java", "Vaga para backend", "Empresa X", "VR, VA", 7500.0);
    }

    private CandidatoDTO novoCandidatoDTO(Long id) {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setId(id);
        candidatoDTO.setNome("Maria");
        candidatoDTO.setEmail("maria@email.com");
        return candidatoDTO;
    }

    private CandidatoVagaDTO novaCandidaturaDTO(Long id, Long candidatoId, Long vagaId) {
        CandidatoVagaDTO candidaturaDTO = new CandidatoVagaDTO();
        candidaturaDTO.setId(id);
        candidaturaDTO.setCandidatoId(candidatoId);
        candidaturaDTO.setVaga(novaVagaDTO(vagaId));
        candidaturaDTO.setDataInscricao(LocalDateTime.of(2026, 9, 3, 13, 0));
        return candidaturaDTO;
    }
}
