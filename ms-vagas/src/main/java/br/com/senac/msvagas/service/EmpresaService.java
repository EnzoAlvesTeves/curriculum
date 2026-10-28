package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CreateEmpresaRequest;
import br.com.senac.msvagas.dto.EmpresaResponse;
import br.com.senac.msvagas.dto.UpdateEmpresaRequest;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.repository.EmpresaRepository;
import br.com.senac.msvagas.repository.entity.EmpresaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioContextService usuarioContextService;

    @Transactional
    public EmpresaResponse criar(CreateEmpresaRequest request, String authorization) {
        validarRh(usuarioContextService.buscarUsuarioMe(authorization));

        EmpresaEntity entity = new EmpresaEntity();
        entity.setNome(request.nome());
        entity.setEstado(request.estado());
        entity.setCidade(request.cidade());
        entity.setBairro(request.bairro());
        return toResponse(empresaRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> listar() {
        return empresaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpresaResponse buscarPorId(Long id) {
        return toResponse(buscaEmpresa(id));
    }

    @Transactional
    public EmpresaResponse atualizar(Long id, UpdateEmpresaRequest request, String authorization) {
        validarRh(usuarioContextService.buscarUsuarioMe(authorization));

        EmpresaEntity entity = buscaEmpresa(id);
        entity.setNome(request.nome());
        entity.setEstado(request.estado());
        entity.setCidade(request.cidade());
        entity.setBairro(request.bairro());
        return toResponse(empresaRepository.save(entity));
    }

    @Transactional
    public void excluir(Long id, String authorization) {
        validarRh(usuarioContextService.buscarUsuarioMe(authorization));

        EmpresaEntity entity = buscaEmpresa(id);
        empresaRepository.delete(entity);
    }

    private EmpresaEntity buscaEmpresa(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada"));
    }

    private void validarRh(UsuarioMeResponse usuario) {
        if (usuario.tipo() == null || !usuario.tipo().ehRh()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuarios do tipo RH podem cadastrar, alterar e deletar empresas");
        }
    }

    private EmpresaResponse toResponse(EmpresaEntity entity) {
        return new EmpresaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getBairro(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

