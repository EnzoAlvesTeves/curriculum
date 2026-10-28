CREATE TABLE endereco (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_candidato    BIGINT NOT NULL,
    rua             VARCHAR(500) NOT NULL,
    numero          VARCHAR(255),
    complemento     VARCHAR(255),
    cidade          VARCHAR(100) NOT NULL,
    estado          CHAR(100) NOT NULL,
    cep             VARCHAR(10) NOT NULL,
    bairro          VARCHAR(255),
    latitude        DECIMAL(9,6) NULL,
    longitude       DECIMAL(9,6) NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_candidato) REFERENCES candidato(id) ON DELETE CASCADE
);