CREATE TABLE usuario (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    keycloak_user_id    VARCHAR(36) NOT NULL UNIQUE,
    nome                VARCHAR(100) NOT NULL,
    sobrenome           VARCHAR(150) NOT NULL,
    email               VARCHAR(150) NOT NULL UNIQUE,
    telefone            VARCHAR(30),
    tipo                VARCHAR(20) NOT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NULL
);