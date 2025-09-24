package br.com.senac.mscurriculum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Long> {
}
