package br.com.senac.msvagas.repository;

import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatoVagaRepository extends JpaRepository<CandidatoVagaEntity, Long> {
	List<CandidatoVagaEntity> findByCandidatoId(Long candidatoId);

	List<CandidatoVagaEntity> findByVagaId(Long vagaId);
}
