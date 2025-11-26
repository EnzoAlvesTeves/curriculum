package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducacaoRepository extends JpaRepository<EducacaoEntity, Long> {
	List<EducacaoEntity> findByCandidatoId(Long candidatoId);
}
