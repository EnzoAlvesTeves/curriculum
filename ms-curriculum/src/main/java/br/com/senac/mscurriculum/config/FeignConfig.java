package br.com.senac.mscurriculum.config;

import br.com.senac.mscurriculum.client.MsUsuarioClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(MsUsuarioFeignProperties.class)
public class FeignConfig {

    @Bean
    public MsUsuarioClient msUsuarioClient(MsUsuarioFeignProperties props, ObjectMapper objectMapper) {
        String url = defaultString(props.url(), "http://localhost:8091");
        String loggerLevel = defaultString(props.loggerLevel(), "FULL");
        int connectTimeoutMillis = defaultInt(props.connectTimeout(), 5000);
        int readTimeoutMillis = defaultInt(props.readTimeout(), 10000);

        return Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new Slf4jLogger(MsUsuarioClient.class))
                .logLevel(parseLevel(loggerLevel))
                .options(new Request.Options(
                        connectTimeoutMillis,
                        TimeUnit.MILLISECONDS,
                        readTimeoutMillis,
                        TimeUnit.MILLISECONDS,
                        true
                ))
                .target(MsUsuarioClient.class, url);
    }

    private Logger.Level parseLevel(String value) {
        if (value == null || value.isBlank()) {
            return Logger.Level.FULL;
        }

        try {
            return Logger.Level.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Logger.Level.FULL;
        }
    }

    private String defaultString(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private int defaultInt(Integer value, int fallback) {
        return value == null ? fallback : value;
    }
}
