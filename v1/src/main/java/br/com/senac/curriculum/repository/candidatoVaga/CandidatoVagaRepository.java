package br.com.senac.curriculum.repository.candidatoVaga;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatoVagaRepository extends JpaRepository<CandidatoVagaEntity, Long> {
    List<CandidatoVagaEntity> findByVaga(VagaEntity vaga);
    List<CandidatoVagaEntity> findByCandidato(CandidatoEntity candidato);
}
