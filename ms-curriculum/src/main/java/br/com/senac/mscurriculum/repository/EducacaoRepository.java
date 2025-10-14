package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducacaoRepository extends JpaRepository<EducacaoEntity, Long> {
}
