CREATE TABLE experiencia (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cargo           VARCHAR(100) NOT NULL,
    empresa         VARCHAR(100) NOT NULL,
    resumo          TEXT,
    data_inicio     DATE NOT NULL,
    data_fim        DATE,
    id_candidato    BIGINT NOT NULL,
    FOREIGN KEY (id_candidato) REFERENCES candidato (id) ON DELETE CASCADE
);