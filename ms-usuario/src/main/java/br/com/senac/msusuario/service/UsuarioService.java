package br.com.senac.msusuario.service;

import br.com.senac.msusuario.dto.UsuarioDTO;
import br.com.senac.msusuario.repository.UsuarioRepository;
import br.com.senac.msusuario.repository.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioService {

	private UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public UsuarioDTO create(UsuarioDTO usuarioDTO) {
		UsuarioEntity usuarioEntity = usuarioDTO.toEntity();

		UsuarioEntity novoUsuario = usuarioRepository.save(usuarioEntity);

		return new UsuarioDTO(novoUsuario);
	}

	public UsuarioDTO getById(Long id) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

		return new UsuarioDTO(usuarioEntity);
	}

	public UsuarioDTO update(UsuarioDTO usuarioDTO) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioDTO.getId())
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
		usuarioEntity.setId(usuarioDTO.getId());
		usuarioEntity.setNome(usuarioDTO.getNome());
		usuarioEntity.setEmail(usuarioDTO.getEmail());
		usuarioEntity.setSenha(usuarioDTO.getSenha());

		UsuarioEntity usuarioAlterado = usuarioRepository.save(usuarioEntity);

		return new UsuarioDTO(usuarioAlterado);
	}

	public void delete(Long id) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

		usuarioRepository.delete(usuarioEntity);
	}
}
