package br.com.senac.msusuario.repository;

import br.com.senac.msusuario.repository.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	Optional<UsuarioEntity> findByEmail(String email);
	Optional<UsuarioEntity> findByKeycloakUserId(String keycloakUserId);
}
