package br.com.senac.msvagas.repository;

import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<VagaEntity, Long> {
}
