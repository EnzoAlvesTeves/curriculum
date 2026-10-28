package br.com.senac.msvagas.repository;

import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VagaRepository extends JpaRepository<VagaEntity, Long> {
    List<VagaEntity> findByIdEmpresa(Long idEmpresa);

    List<VagaEntity> findByCreatedBy(Long createdBy);
}

