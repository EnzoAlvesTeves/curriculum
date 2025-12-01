package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.controller.request.CadastroUsuario;
import br.com.senac.bffcurriculum.controller.request.EsqueciSenha;
import br.com.senac.bffcurriculum.controller.request.Login;
import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioClient usuarioClient;

    UsuarioService(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public UsuarioDTO cadastroUsuario(CadastroUsuario cadastroUsuario) {
        if (!Objects.equals(cadastroUsuario.getSenha(), cadastroUsuario.getConfirmarSenha())) {
            throw new RuntimeException("Confirmação de senha inválida!");
        }

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome(cadastroUsuario.getNome());
        usuario.setEmail(cadastroUsuario.getEmail());
        usuario.setSenha(cadastroUsuario.getSenha());

        return usuarioClient.create(usuario);
    }

    public String esqueciMinhaSenha(EsqueciSenha esqueciSenha) {
        UsuarioDTO usuario = usuarioClient.getByEmail(esqueciSenha.getEmail());
        if (usuario == null) {
            throw new RuntimeException("Não foi encontrado usuário para o email informado!");
        }

        return usuario.getSenha();
    }

    public UsuarioDTO getById(Long id) {
        return usuarioClient.getById(id);
    }

    public UsuarioDTO login(Login login) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(login.getEmail());
        loginRequestDTO.setSenha(login.getSenha());

        return usuarioClient.login(loginRequestDTO);
    }
}
