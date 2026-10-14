package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CreateEmpresaRequest;
import br.com.senac.msvagas.dto.EmpresaResponse;
import br.com.senac.msvagas.dto.TipoUsuario;
import br.com.senac.msvagas.dto.UpdateEmpresaRequest;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.repository.EmpresaRepository;
import br.com.senac.msvagas.repository.entity.EmpresaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
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
class EmpresaServiceTest {

    private static final String AUTHORIZATION = "Bearer token-123";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 9, 3, 13, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2026, 9, 3, 14, 0);

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioContextService usuarioContextService;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    @DisplayName("Deve criar empresa com sucesso para usuario RH")
    void deveCriarEmpresaComSucessoParaUsuarioRh() {
        CreateEmpresaRequest request = new CreateEmpresaRequest("Empresa X", "SP", "São Paulo", "Centro");
        EmpresaEntity entitySalva = novaEmpresaEntity(10L, request.nome(), request.estado(), request.cidade(), request.bairro());
        UsuarioMeResponse usuarioRh = novoUsuarioRh();

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(empresaRepository.save(any(EmpresaEntity.class))).thenReturn(entitySalva);

        EmpresaResponse actualResponse = empresaService.criar(request, AUTHORIZATION);

        assertNotNull(actualResponse);
        assertEquals(10L, actualResponse.id());
        assertEquals(request.nome(), actualResponse.nome());
        assertEquals(request.estado(), actualResponse.estado());
        assertEquals(request.cidade(), actualResponse.cidade());
        assertEquals(request.bairro(), actualResponse.bairro());

        ArgumentCaptor<EmpresaEntity> captor = ArgumentCaptor.forClass(EmpresaEntity.class);
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).save(captor.capture());
        assertEquals(request.nome(), captor.getValue().getNome());
        assertEquals(request.estado(), captor.getValue().getEstado());
        assertEquals(request.cidade(), captor.getValue().getCidade());
        assertEquals(request.bairro(), captor.getValue().getBairro());
    }

    @Test
    @DisplayName("Deve listar empresas com sucesso")
    void deveListarEmpresasComSucesso() {
        EmpresaEntity empresa1 = novaEmpresaEntity(1L, "Empresa A", "SP", "São Paulo", "Centro");
        EmpresaEntity empresa2 = novaEmpresaEntity(2L, "Empresa B", "RJ", "Rio de Janeiro", "Copacabana");
        when(empresaRepository.findAll()).thenReturn(List.of(empresa1, empresa2));

        List<EmpresaResponse> actualResponse = empresaService.listar();

        assertEquals(2, actualResponse.size());
        assertEquals("Empresa A", actualResponse.get(0).nome());
        assertEquals("Empresa B", actualResponse.get(1).nome());
        verify(empresaRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar empresa por id com sucesso")
    void deveBuscarEmpresaPorIdComSucesso() {
        EmpresaEntity entity = novaEmpresaEntity(1L, "Empresa A", "SP", "São Paulo", "Centro");
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(entity));

        EmpresaResponse actualResponse = empresaService.buscarPorId(1L);

        assertEquals(1L, actualResponse.id());
        assertEquals("Empresa A", actualResponse.nome());
        verify(empresaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando empresa nao existir ao buscar por id")
    void deveLancarNotFoundQuandoEmpresaNaoExistirAoBuscarPorId() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> empresaService.buscarPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Empresa nao encontrada", exception.getReason());
        verify(empresaRepository).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar empresa com sucesso para usuario RH")
    void deveAtualizarEmpresaComSucessoParaUsuarioRh() {
        UpdateEmpresaRequest request = new UpdateEmpresaRequest("Empresa Atualizada", "MG", "Belo Horizonte", "Savassi");
        EmpresaEntity entity = novaEmpresaEntity(5L, "Empresa Antiga", "SP", "São Paulo", "Centro");
        UsuarioMeResponse usuarioRh = novoUsuarioRh();

        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(empresaRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(empresaRepository.save(any(EmpresaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmpresaResponse actualResponse = empresaService.atualizar(5L, request, AUTHORIZATION);

        assertEquals(5L, actualResponse.id());
        assertEquals(request.nome(), actualResponse.nome());
        assertEquals(request.estado(), actualResponse.estado());
        assertEquals(request.cidade(), actualResponse.cidade());
        assertEquals(request.bairro(), actualResponse.bairro());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).findById(5L);
        verify(empresaRepository).save(entity);
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando usuario nao for RH ao criar empresa")
    void deveLancarForbiddenQuandoUsuarioNaoForRhAoCriarEmpresa() {
        CreateEmpresaRequest request = new CreateEmpresaRequest("Empresa X", "SP", "São Paulo", "Centro");
        UsuarioMeResponse usuarioCandidato = novoUsuarioCandidato();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioCandidato);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> empresaService.criar(request, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar empresas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(empresaRepository);
    }

    @Test
    @DisplayName("Deve excluir empresa com sucesso para usuario RH")
    void deveExcluirEmpresaComSucessoParaUsuarioRh() {
        EmpresaEntity entity = novaEmpresaEntity(7L, "Empresa A", "SP", "São Paulo", "Centro");
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(empresaRepository.findById(7L)).thenReturn(Optional.of(entity));

        empresaService.excluir(7L, AUTHORIZATION);

        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).findById(7L);
        verify(empresaRepository).delete(entity);
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND quando empresa nao existir ao excluir")
    void deveLancarNotFoundQuandoEmpresaNaoExistirAoExcluir() {
        UsuarioMeResponse usuarioRh = novoUsuarioRh();
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioRh);
        when(empresaRepository.findById(11L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> empresaService.excluir(11L, AUTHORIZATION));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Empresa nao encontrada", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verify(empresaRepository).findById(11L);
        verify(empresaRepository, never()).delete(any(EmpresaEntity.class));
    }

    private EmpresaEntity novaEmpresaEntity(Long id, String nome, String estado, String cidade, String bairro) {
        EmpresaEntity entity = new EmpresaEntity();
        entity.setId(id);
        entity.setNome(nome);
        entity.setEstado(estado);
        entity.setCidade(cidade);
        entity.setBairro(bairro);
        entity.setCreatedAt(CREATED_AT);
        entity.setUpdatedAt(UPDATED_AT);
        return entity;
    }

    private UsuarioMeResponse novoUsuarioRh() {
        return new UsuarioMeResponse(1L, TipoUsuario.RH, "Ana", "Silva", "ana@email.com");
    }

    private UsuarioMeResponse novoUsuarioCandidato() {
        return new UsuarioMeResponse(2L, TipoUsuario.CANDIDATO, "João", "Souza", "joao@email.com");
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando tipo de usuario eh null ao atualizar empresa")
    void deveLancarForbiddenQuandoTipoUsuarioEhNullAoAtualizarEmpresa() {
        UpdateEmpresaRequest request = new UpdateEmpresaRequest("Empresa Atualizada", "MG", "Belo Horizonte", "Savassi");
        UsuarioMeResponse usuarioComTipoNull = new UsuarioMeResponse(2L, null, "João", "Souza", "joao@email.com");
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioComTipoNull);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> empresaService.atualizar(5L, request, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar empresas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(empresaRepository);
    }

    @Test
    @DisplayName("Deve lancar FORBIDDEN quando tipo de usuario eh null ao excluir empresa")
    void deveLancarForbiddenQuandoTipoUsuarioEhNullAoExcluirEmpresa() {
        UsuarioMeResponse usuarioComTipoNull = new UsuarioMeResponse(2L, null, "João", "Souza", "joao@email.com");
        when(usuarioContextService.buscarUsuarioMe(AUTHORIZATION)).thenReturn(usuarioComTipoNull);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> empresaService.excluir(7L, AUTHORIZATION));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Apenas usuarios do tipo RH podem cadastrar, alterar e deletar empresas", exception.getReason());
        verify(usuarioContextService).buscarUsuarioMe(AUTHORIZATION);
        verifyNoInteractions(empresaRepository);
    }
}
