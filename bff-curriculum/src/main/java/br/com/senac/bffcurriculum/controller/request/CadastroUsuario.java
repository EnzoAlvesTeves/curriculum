package br.com.senac.bffcurriculum.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroUsuario {
    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
}
