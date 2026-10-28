package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.VagaClient;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaClient vagaClient;

    @InjectMocks
    private VagaService vagaService;

    @Test
    @DisplayName("Deve cadastrar vaga delegando ao client")
    void deveCadastrarVagaDelegandoAoClient() {
        VagaDTO vagaDTO = novaVagaDTO(1L);
        when(vagaClient.create(vagaDTO)).thenReturn(vagaDTO);

        vagaService.cadastrar(vagaDTO);

        verify(vagaClient).create(vagaDTO);
    }

    @Test
    @DisplayName("Deve listar vagas delegando ao client")
    void deveListarVagasDelegandoAoClient() {
        List<VagaDTO> expectedResponse = List.of(novaVagaDTO(1L), novaVagaDTO(2L));
        when(vagaClient.getall()).thenReturn(expectedResponse);

        List<VagaDTO> actualResponse = vagaService.getAll();

        assertSame(expectedResponse, actualResponse);
        assertEquals(2, actualResponse.size());
        verify(vagaClient).getall();
    }

    @Test
    @DisplayName("Deve buscar vaga por id delegando ao client")
    void deveBuscarVagaPorIdDelegandoAoClient() {
        VagaDTO expectedResponse = novaVagaDTO(10L);
        when(vagaClient.getById(10L)).thenReturn(expectedResponse);

        VagaDTO actualResponse = vagaService.getById(10L);

        assertSame(expectedResponse, actualResponse);
        verify(vagaClient).getById(10L);
    }

    private VagaDTO novaVagaDTO(Long id) {
        return new VagaDTO(
                id,
                "Desenvolvedor Java",
                "Vaga para backend",
                "Empresa X",
                "VR, VA",
                7500.0
        );
    }
}
