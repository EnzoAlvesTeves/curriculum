package br.com.senac.mscurriculum.repository;

import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Long> {
}
