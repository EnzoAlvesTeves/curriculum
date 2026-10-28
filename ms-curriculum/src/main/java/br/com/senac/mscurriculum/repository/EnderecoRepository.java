package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Long> {
    Optional<EnderecoEntity> findByIdCandidato(Long idCandidato);
}

