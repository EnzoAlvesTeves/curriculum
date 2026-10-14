package br.com.senac.msusuario.service;

import br.com.senac.msusuario.config.KeycloakProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    private KeycloakService keycloakService;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.build()).thenReturn(restClient);
        keycloakService = new KeycloakService(restClientBuilder, keycloakProperties);
    }

    @Test
    @DisplayName("Deve validar construtor de KeycloakService")
    void deveValidarConstrutor() {
        assertNotNull(keycloakService);
    }
}