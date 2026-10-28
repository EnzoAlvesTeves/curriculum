package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.controller.request.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    @Test
    @DisplayName("Deve abrir a tela de login com objeto vazio no model")
    void deveAbrirATelaDeLoginComObjetoVazioNoModel() {
        String viewName = loginController.login(model);

        assertEquals("login", viewName);
        verify(model).addAttribute("login", new Login());
    }
}
