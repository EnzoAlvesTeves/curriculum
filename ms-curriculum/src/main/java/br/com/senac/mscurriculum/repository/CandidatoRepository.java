package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Long> {
    Optional<CandidatoEntity> findByUsuarioId(Long usuarioId);
}
