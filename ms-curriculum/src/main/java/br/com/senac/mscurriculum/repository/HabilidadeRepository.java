package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabilidadeRepository extends JpaRepository<HabilidadeEntity, Long> {
    List<HabilidadeEntity> findByIdCandidato(Long idCandidato);
    void deleteByIdCandidato(Long idCandidato);
}
