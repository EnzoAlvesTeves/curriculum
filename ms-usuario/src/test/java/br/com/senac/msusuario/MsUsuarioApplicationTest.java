package br.com.senac.msusuario;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class MsUsuarioApplicationTest {

    @Test
    void deveExecutarMainSemErro() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            MsUsuarioApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(MsUsuarioApplication.class, new String[]{}));
        }
    }
}
