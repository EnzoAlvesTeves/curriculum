package br.com.senac.bffcurriculum.controller.request;

import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequestDTO toDTO() {
        return new LoginRequestDTO(email, senha);
    }
}
