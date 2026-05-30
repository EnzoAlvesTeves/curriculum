package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Long> {
    Optional<CandidatoEntity> findByIdUsuario(Long idUsuario);
}

