package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienciaRepository extends JpaRepository<ExperienciaEntity, Long> {
    List<ExperienciaEntity> findByIdCandidato(Long idCandidato);
    void deleteByIdCandidato(Long idCandidato);
}
