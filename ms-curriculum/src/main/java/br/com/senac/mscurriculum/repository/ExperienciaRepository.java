package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienciaRepository extends JpaRepository<ExperienciaEntity, Long> {
	List<ExperienciaEntity> findByCandidatoId(Long candidatoId);
}
