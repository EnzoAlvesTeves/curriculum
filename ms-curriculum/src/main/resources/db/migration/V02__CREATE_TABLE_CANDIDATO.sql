CREATE TABLE candidato (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome                VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    sexo                VARCHAR(100),
    telefone            VARCHAR(15),
    data_nascimento     DATE NOT NULL,
    resumo_profissional TEXT,
    id_usuario          BIGINT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);