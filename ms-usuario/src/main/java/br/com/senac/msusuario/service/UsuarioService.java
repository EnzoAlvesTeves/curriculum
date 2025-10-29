package br.com.senac.msusuario.service;

import br.com.senac.msusuario.dto.UsuarioDTO;
import br.com.senac.msusuario.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        // Lógica para criar um novo usuário
        return null; // Retornar o usuário criado
    }

    public UsuarioDTO getById(Long id) {
        // Lógica para obter um usuário pelo ID
        return null; // Retornar o usuário encontrado
    }

    public UsuarioDTO update(UsuarioDTO usuarioDTO) {
        // Lógica para atualizar um usuário existente
        return null; // Retornar o usuário atualizado
    }

    public void delete(Long id) {
        // Lógica para deletar um usuário
    }
}
