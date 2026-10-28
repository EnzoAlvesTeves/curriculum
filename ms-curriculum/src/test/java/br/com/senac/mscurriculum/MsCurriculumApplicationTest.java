package br.com.senac.mscurriculum;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class MsCurriculumApplicationTest {

    @Test
    void deveExecutarMainSemErro() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            MsCurriculumApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(MsCurriculumApplication.class, new String[]{}));
        }
    }
}
