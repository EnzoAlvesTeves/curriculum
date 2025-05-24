package br.com.senac.curriculum.service;

import br.com.senac.curriculum.dto.UsuarioDTO;
import br.com.senac.curriculum.repository.usuario.UsuarioEntity;
import br.com.senac.curriculum.repository.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO cadastrar(UsuarioDTO usuarioDTO) {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if (usuarioEntity.isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());

        usuario = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuario);
    }

    public UsuarioDTO login(String email, String senha) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            if (usuario.get().getSenha().equals(senha)) {
                return new UsuarioDTO(usuario.get());
            } else {
                throw new RuntimeException("Senha incorreta");
            }
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }

    }

    public String esqueciSenha(String email) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            return usuario.get().getSenha();
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }
}
