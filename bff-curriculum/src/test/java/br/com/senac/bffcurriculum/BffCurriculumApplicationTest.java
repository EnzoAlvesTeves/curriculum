package br.com.senac.bffcurriculum;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class BffCurriculumApplicationTest {

    @Test
    void deveExecutarMainSemErro() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            BffCurriculumApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(BffCurriculumApplication.class, new String[]{}));
        }
    }
}
