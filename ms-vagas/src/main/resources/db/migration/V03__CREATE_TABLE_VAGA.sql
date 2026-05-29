CREATE TABLE vaga (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR (255) NOT NULL,
    descricao   TEXT NOT NULL,
    salario     DECIMAL (10, 2),
    beneficios  TEXT,
    id_empresa  BIGINT NOT NULL,
    created_by  BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_empresa) REFERENCES empresa(id) ON DELETE CASCADE
);