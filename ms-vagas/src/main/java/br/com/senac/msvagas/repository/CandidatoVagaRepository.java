package br.com.senac.msvagas.repository;

import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatoVagaRepository extends JpaRepository<CandidatoVagaEntity, Long> {
    boolean existsByIdUsuarioAndIdVaga(Long idUsuario, Long idVaga);

    Optional<CandidatoVagaEntity> findByIdUsuarioAndIdVaga(Long idUsuario, Long idVaga);

    List<CandidatoVagaEntity> findByIdUsuario(Long idUsuario);
}

