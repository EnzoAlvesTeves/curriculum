package br.com.senac.msvagas;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class MsVagasApplicationTest {

    @Test
    void deveExecutarMainSemErro() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            MsVagasApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(MsVagasApplication.class, new String[]{}));
        }
    }
}
