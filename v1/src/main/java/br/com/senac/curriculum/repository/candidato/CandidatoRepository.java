package br.com.senac.curriculum.repository.candidato;

import br.com.senac.curriculum.repository.usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<CandidatoEntity, Long> {
    Optional<CandidatoEntity> findByUsuario(UsuarioEntity usuario);
}
