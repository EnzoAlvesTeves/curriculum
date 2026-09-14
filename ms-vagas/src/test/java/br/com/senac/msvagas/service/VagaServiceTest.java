package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CandidaturaResponse;
import br.com.senac.msvagas.dto.CreateVagaRequest;
import br.com.senac.msvagas.dto.TipoUsuario;
import br.com.senac.msvagas.dto.UpdateVagaRequest;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.dto.UsuarioResponse;
import br.com.senac.msvagas.dto.VagaResponse;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.EmpresaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    private static final String AUTHORIZATION = "Bearer token-123";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 9, 3, 13, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2026, 9, 3, 14, 0);

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private CandidatoVagaRepository candidatoVagaRepository;

    @Mock
    private UsuarioContextService usuarioContextService;

    @InjectMocks
    private VagaService vagaService;

    @Test
    @DisplayName("Deve criar vaga com sucesso para usuario RH")
    void deveCriarVagaComSucessoParaUsuarioRh() {
        CreateVagaRequest request = novaCreateVagaRequest();
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        VagaEntity salva = novaVagaEntity(1L, request.titulo(), request.descricao(), request.salario(), request.beneficios(), request.idEmpresa(), usuarioRh.id());

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(empresaRepository.existsById(request.idEmpresa())).thenReturn(true);
        when(vagaRepository.save(any(VagaEntity.class))).thenReturn(salva);

        VagaResponse actualResponse = vagaService.criar(request, AUTHORIZATION);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals(request.titulo(), actualResponse.titulo());
        assertEquals(request.descricao(), actualResponse.descricao());
        assertEquals(request.salario(), actualResponse.salario());
        assertEquals(request.beneficios(), actualResponse.beneficios());
        assertEquals(request.idEmpresa(), actualResponse.idEmpresa());
        assertEquals(usuarioRh.id(), actualResponse.createdBy());

        ArgumentCaptor<VagaEntity> captor = ArgumentCaptor.forClass(VagaEntity.class);
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).existsById(request.idEmpresa());
        verify(vagaRepository).save(captor.capture());
        assertEquals(request.titulo(), captor.getValue().getTitulo());
        assertEquals(request.descricao(), captor.getValue().getDescricao());
        assertEquals(request.salario(), captor.getValue().getSalario());
        assertEquals(request.beneficios(), captor.getValue().getBeneficios());
        assertEquals(request.idEmpresa(), captor.getValue().getIdEmpresa());
        assertEquals(usuarioRh.id(), captor.getValue().getCreatedBy());
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for RH ao criar vaga")
    void deveLancarForbiddenQuandoUsuarioNaoForRhAoCriarVaga() {
        CreateVagaRequest request = novaCreateVagaRequest();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioCandidato());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.criar(request, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar vagas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(empresaRepository, vagaRepository, candidatoVagaRepository);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando empresa nao existir ao criar vaga")
    void deveLancarNotFoundQuandoEmpresaNaoExistirAoCriarVaga() {
        CreateVagaRequest request = novaCreateVagaRequest();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());
        when(empresaRepository.existsById(request.idEmpresa())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.criar(request, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Empresa nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).existsById(request.idEmpresa());
        verify(vagaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar vagas com sucesso por empresa")
    void deveListarVagasComSucessoPorEmpresa() {
        List<VagaEntity> entidades = List.of(
                novaVagaEntity(1L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 100L),
                novaVagaEntity(2L, "Dev Java Sr", "Backend Sr", new BigDecimal("9000.00"), "VR, VA", 10L, 100L)
        );
        when(vagaRepository.findByIdEmpresa(10L)).thenReturn(entidades);

        List<VagaResponse> actualResponse = vagaService.buscarPorEmpresa(10L);

        assertEquals(2, actualResponse.size());
        assertEquals("Dev Java", actualResponse.get(0).titulo());
        assertEquals("Dev Java Sr", actualResponse.get(1).titulo());
        verify(vagaRepository).findByIdEmpresa(10L);
    }

    @Test
    @DisplayName("Deve listar minhas vagas criadas com sucesso")
    void deveListarMinhasVagasCriadasComSucesso() {
        UsuarioMeResponse usuario = novoUsuarioRh();
        List<VagaEntity> entidades = List.of(
                novaVagaEntity(1L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, usuario.id()),
                novaVagaEntity(2L, "Dev Java Sr", "Backend Sr", new BigDecimal("9000.00"), "VR, VA", 10L, usuario.id())
        );
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(vagaRepository.findByCreatedBy(usuario.id())).thenReturn(entidades);

        List<VagaResponse> actualResponse = vagaService.buscarMinhasCriadas(AUTHORIZATION);

        assertEquals(2, actualResponse.size());
        assertEquals("Dev Java", actualResponse.get(0).titulo());
        assertEquals("Dev Java Sr", actualResponse.get(1).titulo());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findByCreatedBy(usuario.id());
    }

    @Test
    @DisplayName("Deve buscar vaga por id com sucesso ao atualizar")
    void deveAtualizarVagaComSucessoParaUsuarioRh() {
        UpdateVagaRequest request = novaUpdateVagaRequest();
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        VagaEntity entity = novaVagaEntity(5L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, usuarioRh.id());
        VagaEntity salva = novaVagaEntity(5L, request.titulo(), request.descricao(), request.salario(), request.beneficios(), request.idEmpresa(), usuarioRh.id());

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(vagaRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(empresaRepository.existsById(request.idEmpresa())).thenReturn(true);
        when(vagaRepository.save(entity)).thenReturn(salva);

        VagaResponse actualResponse = vagaService.atualizar(5L, request, AUTHORIZATION);

        assertEquals(5L, actualResponse.id());
        assertEquals(request.titulo(), actualResponse.titulo());
        assertEquals(request.descricao(), actualResponse.descricao());
        assertEquals(request.salario(), actualResponse.salario());
        assertEquals(request.beneficios(), actualResponse.beneficios());
        assertEquals(request.idEmpresa(), actualResponse.idEmpresa());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(5L);
        verify(empresaRepository).existsById(request.idEmpresa());
        verify(vagaRepository).save(entity);
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for RH ao atualizar vaga")
    void deveLancarForbiddenQuandoUsuarioNaoForRhAoAtualizarVaga() {
        UpdateVagaRequest request = novaUpdateVagaRequest();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioCandidato());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.atualizar(5L, request, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar vagas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(empresaRepository, vagaRepository, candidatoVagaRepository);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando vaga nao existir ao atualizar")
    void deveLancarNotFoundQuandoVagaNaoExistirAoAtualizar() {
        UpdateVagaRequest request = novaUpdateVagaRequest();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());
        when(vagaRepository.findById(5L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.atualizar(5L, request, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Vaga nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(5L);
        verifyNoInteractions(empresaRepository);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando empresa nao existir ao atualizar")
    void deveLancarNotFoundQuandoEmpresaNaoExistirAoAtualizar() {
        UpdateVagaRequest request = novaUpdateVagaRequest();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());
        when(vagaRepository.findById(5L)).thenReturn(Optional.of(novaVagaEntity(5L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 1L)));
        when(empresaRepository.existsById(request.idEmpresa())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.atualizar(5L, request, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Empresa nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(5L);
        verify(empresaRepository).existsById(request.idEmpresa());
        verify(vagaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir vaga com sucesso para usuario RH")
    void deveExcluirVagaComSucessoParaUsuarioRh() {
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        VagaEntity entity = novaVagaEntity(7L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, usuarioRh.id());
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(vagaRepository.findById(7L)).thenReturn(Optional.of(entity));

        vagaService.deletar(7L, AUTHORIZATION);

        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(7L);
        verify(vagaRepository).delete(entity);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando vaga nao existir ao excluir")
    void deveLancarNotFoundQuandoVagaNaoExistirAoExcluir() {
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());
        when(vagaRepository.findById(7L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.deletar(7L, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Vaga nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(7L);
        verify(vagaRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve candidatar usuario com sucesso")
    void deveCandidatarUsuarioComSucesso() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        VagaEntity vaga = novaVagaEntity(11L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 100L);
        CandidatoVagaEntity candidaturaSalva = novaCandidaturaEntity(1L, usuario.id(), 11L);

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(vagaRepository.findById(11L)).thenReturn(Optional.of(vaga));
        when(candidatoVagaRepository.existsByIdUsuarioAndIdVaga(usuario.id(), 11L)).thenReturn(false);
        when(candidatoVagaRepository.save(any(CandidatoVagaEntity.class))).thenReturn(candidaturaSalva);

        CandidaturaResponse actualResponse = vagaService.candidatar(11L, AUTHORIZATION);

        assertEquals(1L, actualResponse.id());
        assertEquals(usuario.id(), actualResponse.idUsuario());
        assertEquals(11L, actualResponse.idVaga());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(11L);
        verify(candidatoVagaRepository).existsByIdUsuarioAndIdVaga(usuario.id(), 11L);
        verify(candidatoVagaRepository).save(any(CandidatoVagaEntity.class));
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for candidato ao candidatar")
    void deveLancarForbiddenQuandoUsuarioNaoForCandidatoAoCandidatar() {
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.candidatar(11L, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo CANDIDATO podem se candidatar ou remover candidatura", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(vagaRepository, candidatoVagaRepository);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando vaga nao existir ao candidatar")
    void deveLancarNotFoundQuandoVagaNaoExistirAoCandidatar() {
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioCandidato());
        when(vagaRepository.findById(11L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.candidatar(11L, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Vaga nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(11L);
        verifyNoInteractions(candidatoVagaRepository);
    }

    @Test
    @DisplayName("Deve lancar CONFLICT quando candidatura ja existir")
    void deveLancarConflictQuandoCandidaturaJaExistir() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(vagaRepository.findById(11L)).thenReturn(Optional.of(novaVagaEntity(11L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 100L)));
        when(candidatoVagaRepository.existsByIdUsuarioAndIdVaga(usuario.id(), 11L)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.candidatar(11L, AUTHORIZATION));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Usuario ja se candidatou para essa vaga", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(11L);
        verify(candidatoVagaRepository).existsByIdUsuarioAndIdVaga(usuario.id(), 11L);
        verify(candidatoVagaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover candidatura com sucesso")
    void deveRemoverCandidaturaComSucesso() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        CandidatoVagaEntity candidatura = novaCandidaturaEntity(1L, usuario.id(), 11L);

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(candidatoVagaRepository.findByIdUsuarioAndIdVaga(usuario.id(), 11L)).thenReturn(Optional.of(candidatura));

        vagaService.removerCandidatura(11L, AUTHORIZATION);

        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(candidatoVagaRepository).findByIdUsuarioAndIdVaga(usuario.id(), 11L);
        verify(candidatoVagaRepository).delete(candidatura);
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for candidato ao remover candidatura")
    void deveLancarForbiddenQuandoUsuarioNaoForCandidatoAoRemoverCandidatura() {
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioRh());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.removerCandidatura(11L, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo CANDIDATO podem se candidatar ou remover candidatura", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(candidatoVagaRepository);
        verify(vagaRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando candidatura nao existir ao remover")
    void deveLancarNotFoundQuandoCandidaturaNaoExistirAoRemover() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(candidatoVagaRepository.findByIdUsuarioAndIdVaga(usuario.id(), 11L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.removerCandidatura(11L, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidatura nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(candidatoVagaRepository).findByIdUsuarioAndIdVaga(usuario.id(), 11L);
        verify(candidatoVagaRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve listar minhas candidaturas com sucesso")
    void deveListarMinhasCandidaturasComSucesso() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        List<CandidatoVagaEntity> candidaturas = List.of(
                novaCandidaturaEntity(1L, usuario.id(), 11L),
                novaCandidaturaEntity(2L, usuario.id(), 12L)
        );
        List<VagaEntity> vagas = List.of(
                novaVagaEntity(11L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 100L),
                novaVagaEntity(12L, "Dev Java Sr", "Backend Sr", new BigDecimal("9000.00"), "VR, VA", 10L, 100L)
        );

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(candidatoVagaRepository.findByIdUsuario(usuario.id())).thenReturn(candidaturas);
        when(vagaRepository.findAllById(List.of(11L, 12L))).thenReturn(vagas);

        List<VagaResponse> actualResponse = vagaService.buscarMinhasCandidaturas(AUTHORIZATION);

        assertEquals(2, actualResponse.size());
        assertEquals("Dev Java", actualResponse.get(0).titulo());
        assertEquals("Dev Java Sr", actualResponse.get(1).titulo());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(candidatoVagaRepository).findByIdUsuario(usuario.id());
        verify(vagaRepository).findAllById(List.of(11L, 12L));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver candidaturas")
    void deveRetornarListaVaziaQuandoNaoHouverCandidaturas() {
        UsuarioMeResponse usuario = novoUsuarioCandidato();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuario);
        when(candidatoVagaRepository.findByIdUsuario(usuario.id())).thenReturn(List.of());

        List<VagaResponse> actualResponse = vagaService.buscarMinhasCandidaturas(AUTHORIZATION);

        assertTrue(actualResponse.isEmpty());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(candidatoVagaRepository).findByIdUsuario(usuario.id());
        verifyNoInteractions(vagaRepository);
    }

    @Test
    @DisplayName("Deve listar candidatos por vaga com sucesso")
    void deveListarCandidatosPorVagaComSucesso() {
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        List<CandidatoVagaEntity> candidaturas = List.of(
                novaCandidaturaEntity(1L, 20L, 11L),
                novaCandidaturaEntity(2L, 21L, 11L),
                novaCandidaturaEntity(3L, 20L, 11L)
        );
        List<UsuarioResponse> usuarios = List.of(
                novoUsuarioResponse(20L, TipoUsuario.CANDIDATO),
                novoUsuarioResponse(21L, TipoUsuario.CANDIDATO)
        );
        VagaEntity vaga = novaVagaEntity(11L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, usuarioRh.id());

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(vagaRepository.findById(11L)).thenReturn(Optional.of(vaga));
        when(candidatoVagaRepository.findByIdVaga(11L)).thenReturn(candidaturas);
        when(usuarioContextService.buscarUsuariosPorIds(List.of(20L, 21L), AUTHORIZATION)).thenReturn(usuarios);

        List<UsuarioResponse> actualResponse = vagaService.buscarCandidatosPorVaga(11L, AUTHORIZATION);

        assertEquals(2, actualResponse.size());
        assertEquals(20L, actualResponse.get(0).id());
        assertEquals(21L, actualResponse.get(1).id());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(11L);
        verify(candidatoVagaRepository).findByIdVaga(11L);
        verify(usuarioContextService).buscarUsuariosPorIds(List.of(20L, 21L), AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for RH ao buscar candidatos por vaga")
    void deveLancarForbiddenQuandoUsuarioNaoForRhAoBuscarCandidatosPorVaga() {
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(novoUsuarioCandidato());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> vagaService.buscarCandidatosPorVaga(11L, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar vagas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(vagaRepository, candidatoVagaRepository);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver candidatos na vaga")
    void deveRetornarListaVaziaQuandoNaoHouverCandidatosNaVaga() {
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        VagaEntity vaga = novaVagaEntity(11L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, usuarioRh.id());

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(vagaRepository.findById(11L)).thenReturn(Optional.of(vaga));
        when(candidatoVagaRepository.findByIdVaga(11L)).thenReturn(List.of());

        List<UsuarioResponse> actualResponse = vagaService.buscarCandidatosPorVaga(11L, AUTHORIZATION);

        assertTrue(actualResponse.isEmpty());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(vagaRepository).findById(11L);
        verify(candidatoVagaRepository).findByIdVaga(11L);
        verify(usuarioContextService, never()).buscarUsuariosPorIds(anyList(), anyString());
    }

    @Test
    @DisplayName("Deve listar todas as vagas com sucesso")
    void deveListarTodasAsVagasComSucesso() {
        List<VagaEntity> entidades = List.of(
                novaVagaEntity(1L, "Dev Java", "Backend", new BigDecimal("7000.00"), "VR", 10L, 100L),
                novaVagaEntity(2L, "Dev Java Sr", "Backend Sr", new BigDecimal("9000.00"), "VR, VA", 11L, 101L)
        );
        when(vagaRepository.findAll()).thenReturn(entidades);

        List<VagaResponse> actualResponse = vagaService.buscarVagas(AUTHORIZATION);

        assertEquals(2, actualResponse.size());
        assertEquals("Dev Java", actualResponse.get(0).titulo());
        assertEquals("Dev Java Sr", actualResponse.get(1).titulo());
        verify(vagaRepository).findAll();
    }

    private CreateVagaRequest novaCreateVagaRequest() {
        return new CreateVagaRequest(
                "Desenvolvedor Java",
                "Vaga para backend",
                new BigDecimal("7500.00"),
                "VR, VA",
                10L
        );
    }

    private UpdateVagaRequest novaUpdateVagaRequest() {
        return new UpdateVagaRequest(
                "Desenvolvedor Java Sênior",
                "Vaga para backend sênior",
                new BigDecimal("9500.00"),
                "VR, VA, plano de saúde",
                10L
        );
    }

    private UsuarioMeResponse novoUsuarioRh() {
        return new UsuarioMeResponse(100L, TipoUsuario.RH, "Ana", "Silva", "ana@email.com");
    }

    private UsuarioMeResponse novoUsuarioCandidato() {
        return new UsuarioMeResponse(200L, TipoUsuario.CANDIDATO, "João", "Souza", "joao@email.com");
    }

    private UsuarioResponse novoUsuarioResponse(Long id, TipoUsuario tipo) {
        return new UsuarioResponse(
                id,
                "keycloak-user-" + id,
                "Usuário",
                "Teste",
                "usuario" + id + "@email.com",
                "11999999999",
                tipo,
                CREATED_AT,
                UPDATED_AT
        );
    }

    private VagaEntity novaVagaEntity(Long id, String titulo, String descricao, BigDecimal salario, String beneficios, Long idEmpresa, Long createdBy) {
        VagaEntity entity = new VagaEntity();
        entity.setId(id);
        entity.setTitulo(titulo);
        entity.setDescricao(descricao);
        entity.setSalario(salario);
        entity.setBeneficios(beneficios);
        entity.setIdEmpresa(idEmpresa);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(CREATED_AT);
        entity.setUpdatedAt(UPDATED_AT);
        return entity;
    }

    private CandidatoVagaEntity novaCandidaturaEntity(Long id, Long idUsuario, Long idVaga) {
        CandidatoVagaEntity entity = new CandidatoVagaEntity();
        entity.setId(id);
        entity.setIdUsuario(idUsuario);
        entity.setIdVaga(idVaga);
        entity.setCreatedAt(CREATED_AT);
        entity.setUpdatedAt(UPDATED_AT);
        return entity;
    }
}
