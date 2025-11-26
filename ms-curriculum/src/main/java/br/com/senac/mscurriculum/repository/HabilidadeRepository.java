package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabilidadeRepository extends JpaRepository<HabilidadeEntity, Long> {
	List<HabilidadeEntity> findByCandidatoId(Long candidatoId);
}