package br.com.senac.msusuario.service;

import br.com.senac.msusuario.dto.LoginRequestDTO;
import br.com.senac.msusuario.dto.UsuarioDTO;
import br.com.senac.msusuario.repository.UsuarioRepository;
import br.com.senac.msusuario.repository.entity.UsuarioEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UsuarioService {

	private UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

    @Transactional
	public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        try {
            UsuarioEntity usuarioEntity = usuarioDTO.toEntity();

            UsuarioEntity novoUsuario = usuarioRepository.save(usuarioEntity);

            return new UsuarioDTO(novoUsuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gravar usuário ", e);
        }
	}


	public UsuarioDTO getById(Long id) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

		return new UsuarioDTO(usuarioEntity);
	}

    @Transactional
	public UsuarioDTO update(UsuarioDTO usuarioDTO) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioDTO.getId())
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        try {
            usuarioEntity.setId(usuarioDTO.getId());
            usuarioEntity.setNome(usuarioDTO.getNome());
            usuarioEntity.setEmail(usuarioDTO.getEmail());
            usuarioEntity.setSenha(usuarioDTO.getSenha());

            UsuarioEntity usuarioAlterado = usuarioRepository.save(usuarioEntity);

            return new UsuarioDTO(usuarioAlterado);
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao editar usuário", e);
        }
	}

    @Transactional
	public void delete(Long id) {
		UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        try {
            usuarioRepository.delete(usuarioEntity);
        }   catch (Exception e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
	}

    public UsuarioDTO login(LoginRequestDTO loginRequestDTO) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não existente!"));

        if(usuario.getSenha().equals(loginRequestDTO.getSenha())) {
            return new UsuarioDTO(usuario);
        }

        throw new RuntimeException("Senha inválida!");
    }
}
