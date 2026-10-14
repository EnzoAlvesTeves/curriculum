package br.com.senac.msvagas.config;

import br.com.senac.msvagas.client.MsUsuarioClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FeignConfigTest {

    private final FeignConfig feignConfig = new FeignConfig();

    @Test
    @DisplayName("Deve criar MsUsuarioClient com configuracoes padroes")
    void deveCriarMsUsuarioClientComConfiguracesPadroes() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "FULL",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve criar MsUsuarioClient com url null usando fallback")
    void deveCriarMsUsuarioClientComUrlNullUsandoFallback() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                null,
                "FULL",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve criar MsUsuarioClient com url em branco usando fallback")
    void deveCriarMsUsuarioClientComUrlEmBrancoUsandoFallback() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "   ",
                "FULL",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @ParameterizedTest
    @ValueSource(strings = {"FULL", "full", "Full", "NONE", "BASIC", "HEADERS"})
    @DisplayName("Deve fazer parse de diferentes niveis de logger")
    void deveParseNiveisValidosDeLooger(String level) {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                level,
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar fallback FULL quando logger level eh null")
    void deveUsarFallbackFULLQuandoLoggerLevelEhNull() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                null,
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar fallback FULL quando logger level eh em branco")
    void deveUsarFallbackFULLQuandoLoggerLevelEhEmBranco() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "   ",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar fallback FULL quando logger level eh invalido")
    void deveUsarFallbackFULLQuandoLoggerLevelEhInvalido() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "INVALID_LEVEL",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar timeout padroes quando valores sao null")
    void deveUsarTimeoutPadroesQuandoValoresSaoNull() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "FULL",
                null,
                null
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar valores customizados de timeout")
    void deveUsarValoresCustomizadosDeTimeout() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "FULL",
                15000,
                20000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve parsear logger level NONE corretamente")
    void deveParsearLoggerLevelNoneCorretamente() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "NONE",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar defaultString com valor valido")
    void deveUsarDefaultStringComValorValido() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://custom:9999",
                "FULL",
                5000,
                10000
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve usar defaultInt com valor zero")
    void deveUsarDefaultIntComValorZero() {
        MsUsuarioFeignProperties props = new MsUsuarioFeignProperties(
                "http://localhost:8091",
                "FULL",
                0,
                0
        );

        ObjectMapper objectMapper = new ObjectMapper();
        MsUsuarioClient client = feignConfig.msUsuarioClient(props, objectMapper);

        assertNotNull(client);
    }
}

