package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.mapper.EnderecoMapper;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    private static final Long CANDIDATO_ID = 1L;
    private static final Long ENDERECO_ID = 100L;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private CandidatoAuthorizationService candidatoAuthorizationService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private EnderecoService enderecoService;

    @Test
    @DisplayName("Deve deletar endereco com sucesso")
    void deveDeletarEnderecoComSucesso() {
        EnderecoEntity entity = novoEnderecoEntity(ENDERECO_ID, CANDIDATO_ID);
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.of(entity));

        enderecoService.deletar(CANDIDATO_ID, jwt);

        verify(candidatoAuthorizationService).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
        verify(enderecoRepository).delete(entity);
    }

    @Test
    @DisplayName("Deve lancar RuntimeException quando endereco nao existir ao deletar")
    void deveLancarExceptionQuandoEnderecoNaoExistirAoDeletar() {
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> enderecoService.deletar(CANDIDATO_ID, jwt));

        assertEquals("Endereço não encontrado para o candidato ID: " + CANDIDATO_ID, exception.getMessage());
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
        verify(enderecoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve alterar endereco com sucesso")
    void deveAlterarEnderecoComSucesso() {
        EnderecoEntity entity = novoEnderecoEntity(ENDERECO_ID, CANDIDATO_ID);
        EnderecoDTO dto = novoEnderecoDTO("Rua Nova", "123", "Apto", "São Paulo", "SP", "01310-100", "Centro", new java.math.BigDecimal("10.5"), new java.math.BigDecimal("20.5"));
        EnderecoEntity entityAtualizado = novoEnderecoEntity(ENDERECO_ID, CANDIDATO_ID);
        entityAtualizado.setRua(dto.getRua());
        entityAtualizado.setNumero(dto.getNumero());

        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.of(entity));
        when(enderecoRepository.save(any(EnderecoEntity.class))).thenReturn(entityAtualizado);

        EnderecoDTO result = enderecoService.alterar(CANDIDATO_ID, dto, jwt);

        assertNotNull(result);
        assertEquals(dto.getRua(), result.getRua());
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
        verify(enderecoRepository).save(any(EnderecoEntity.class));
    }

    @Test
    @DisplayName("Deve lancar RuntimeException quando endereco nao existir ao alterar")
    void deveLancarExceptionQuandoEnderecoNaoExistirAoAlterar() {
        EnderecoDTO dto = novoEnderecoDTO("Rua Nova", "123", "Apto", "São Paulo", "SP", "01310-100", "Centro", new java.math.BigDecimal("10.5"), new java.math.BigDecimal("20.5"));
        when(candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)).thenReturn(null);
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> enderecoService.alterar(CANDIDATO_ID, dto, jwt));

        assertEquals("Endereço não encontrado para o candidato ID: " + CANDIDATO_ID, exception.getMessage());
        verify(candidatoAuthorizationService).validarAcessoDoUsuario(CANDIDATO_ID, jwt);
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cadastrar endereco com sucesso sem JWT")
    void deveCadastrarEnderecoComSucessoSemJwt() {
        EnderecoDTO dto = novoEnderecoDTO("Rua Teste", "100", "", "São Paulo", "SP", "01310-100", "Centro", null, null);
        EnderecoEntity entity = novoEnderecoEntity(ENDERECO_ID, CANDIDATO_ID);

        when(enderecoRepository.save(any(EnderecoEntity.class))).thenReturn(entity);

        EnderecoDTO result = enderecoService.cadastrar(CANDIDATO_ID, dto);

        assertNotNull(result);
        assertEquals(ENDERECO_ID, result.getId());
        verify(enderecoRepository).save(any(EnderecoEntity.class));
    }

    @Test
    @DisplayName("Deve buscar endereco por candidato com sucesso")
    void deveBuscarEnderecoPorCandidatoComSucesso() {
        EnderecoEntity entity = novoEnderecoEntity(ENDERECO_ID, CANDIDATO_ID);
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.of(entity));

        EnderecoDTO result = enderecoService.buscarPorCandidato(CANDIDATO_ID);

        assertNotNull(result);
        assertEquals(ENDERECO_ID, result.getId());
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve retornar null quando endereco nao existir ao buscar")
    void deveRetornarNullQuandoEnderecoNaoExistir() {
        when(enderecoRepository.findByIdCandidato(CANDIDATO_ID)).thenReturn(Optional.empty());

        EnderecoDTO result = enderecoService.buscarPorCandidato(CANDIDATO_ID);

        assertEquals(null, result);
        verify(enderecoRepository).findByIdCandidato(CANDIDATO_ID);
    }

    private EnderecoEntity novoEnderecoEntity(Long id, Long candidatoId) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(id);
        entity.setIdCandidato(candidatoId);
        entity.setRua("Rua Teste");
        entity.setNumero("123");
        entity.setComplemento("");
        entity.setCidade("São Paulo");
        entity.setEstado("SP");
        entity.setCep("01310-100");
        entity.setBairro("Centro");
        entity.setLatitude(new java.math.BigDecimal("10.0"));
        entity.setLongitude(new java.math.BigDecimal("20.0"));
        return entity;
    }

    private EnderecoDTO novoEnderecoDTO(String rua, String numero, String complemento, String cidade, String estado, String cep, String bairro, java.math.BigDecimal latitude, java.math.BigDecimal longitude) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setRua(rua);
        dto.setNumero(numero);
        dto.setComplemento(complemento);
        dto.setCidade(cidade);
        dto.setEstado(estado);
        dto.setCep(cep);
        dto.setBairro(bairro);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        return dto;
    }
}
