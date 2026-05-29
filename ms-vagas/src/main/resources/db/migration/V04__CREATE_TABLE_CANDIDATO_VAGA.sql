CREATE TABLE candidato_vaga (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  BIGINT NOT NULL,
    id_vaga     BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_candidato_vaga_usuario_vaga UNIQUE (id_usuario, id_vaga),
    FOREIGN KEY (id_vaga) REFERENCES vaga(id) ON DELETE CASCADE
);