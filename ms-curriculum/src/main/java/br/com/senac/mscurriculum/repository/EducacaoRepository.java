package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducacaoRepository extends JpaRepository<EducacaoEntity, Long> {
    List<EducacaoEntity> findByIdCandidato(Long idCandidato);
    void deleteByIdCandidato(Long idCandidato);
}
