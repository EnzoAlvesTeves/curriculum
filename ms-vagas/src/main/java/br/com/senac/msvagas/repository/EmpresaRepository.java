package br.com.senac.msvagas.repository;

import br.com.senac.msvagas.repository.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {
}

