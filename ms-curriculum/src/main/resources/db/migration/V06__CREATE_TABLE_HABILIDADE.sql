CREATE TABLE habilidade (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao       VARCHAR(255) NOT NULL,
    nivel           VARCHAR(50),
    id_candidato    BIGINT NOT NULL,
    FOREIGN KEY (id_candidato) REFERENCES candidato (id) ON DELETE CASCADE
);