package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Long> {
}
