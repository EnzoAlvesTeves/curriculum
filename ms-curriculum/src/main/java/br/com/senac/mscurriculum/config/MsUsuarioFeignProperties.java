package br.com.senac.mscurriculum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "feign.ms-usuario")
public record MsUsuarioFeignProperties(
        String url,
        String loggerLevel,
        Integer connectTimeout,
        Integer readTimeout
) {
}

